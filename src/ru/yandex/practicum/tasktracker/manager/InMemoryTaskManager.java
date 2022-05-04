package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.enums.TaskType;
import ru.yandex.practicum.tasktracker.enums.TaskStatus;
import ru.yandex.practicum.tasktracker.exception.ManagerTaskValidationException;
import ru.yandex.practicum.tasktracker.model.*;

import java.time.LocalDateTime;
import java.util.*;


//Осторожно в геттерах утекают наружу ссылки task, epic, subtask.
//В ТЗ не сказан про это, так что решил не копировать ещё раз.
//Пришлось добавить закрытые методы, для совместимости со прошлым кодом (getTaskLocal)
//чтобы меньше исправлений делать
public class InMemoryTaskManager implements TaskManager {
    private static Long lastTaskId = 0L;
    private final Map<Long, Task> taskMap = new HashMap<>();
    private final Map<Long, Epic> epicMap = new HashMap<>();
    private final Map<Long, Subtask> subtaskMap = new HashMap<>();
    private final HistoryManager historyManager;

    private final SortedSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(Task::getId));

    public static Long calcNextTaskId() {
        return ++lastTaskId;
    }

    private void updateLastTaskId(long lastTaskId ) {
        if (InMemoryTaskManager.lastTaskId < lastTaskId ) {
            InMemoryTaskManager.lastTaskId = lastTaskId;
        }
    }

    public InMemoryTaskManager() {
        //для совместимости со старыми тестами или кодом
        historyManager = new InMemoryHistoryManager();
    }

    public InMemoryTaskManager(HistoryManager historyManager) {
        //для гибкости, чтобы снаружи можно было связать классы
        this.historyManager = historyManager;
    }

    //Задачи---------------------------------------------------------
    @Override
    public List<Task> getListTask() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
   public void removeAllTask() {
        removePrioritizedTasks(taskMap);
        removeHistory(taskMap);
        taskMap.clear();
    }

    @Override
    public Task getTask(Long id) {
        Task task = getTaskLocal(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    private Task getTaskLocal(Long id) {
        return taskMap.get(id);
    }

    @Override
    public boolean createTask(Task task) {
        if (task == null)  {
            return false;
        }
        // Если задача существует нечего не создаем. Возврат ложь.
        if (getTaskLocal(task.getId()) != null)  {
            return false;
        }
        // локально копируем, чтобы не меняли снаружи
        task = new Task(task);
        if (task.getId() == null) {
            task.setId(calcNextTaskId());
        } else {
            updateLastTaskId(task.getId());
        }
        validationTask(task);
        prioritizedTasks.add(task);
        taskMap.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task == null) {
            return false;
        }
        Task oldTask = getTaskLocal(task.getId());
        // Если задача не существует нечего обновлять. Возврат ложь.
        if (oldTask == null) {
            return false;
        }
        // локально копируем, чтобы не меняли снаружи
        task = new Task(task);
        validationTask(task);
        prioritizedTasks.remove(oldTask);
        prioritizedTasks.add(task);
        taskMap.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean removeTask(Long id) {
       Task task = getTaskLocal(id);
       if (task == null) {
           return false;
       }
       prioritizedTasks.remove(task);
       historyManager.remove(id);
       return taskMap.remove(id) != null;
    }

    //Эпики----------------------------------------------------------
    @Override
    public List<Epic> getListEpic() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public void removeAllEpic() {
        removePrioritizedTasks(epicMap);
        removeHistory(epicMap);
        epicMap.clear();
        removeHistory(subtaskMap);
        subtaskMap.clear();
    }

    @Override
    public Epic getEpic(Long id) {
        Epic epic = getEpicLocal(id);
        if (epic == null) {
            return null;
        }
        historyManager.add(epic);
        return epic;
    }

    private Epic getEpicLocal(Long id) {
       return epicMap.get(id);
    }

    @Override
    public boolean createEpic(Epic epic) {
        if (epic == null) {
            return false;
        }
        //Если Эпик существует нечего не создаем. Возврат ложь.
        if (getEpicLocal(epic.getId()) != null)  {
            return false;
        }
        epic = new Epic(epic);
        if (epic.getId() == null) {
            epic.setId(calcNextTaskId());
        } else {
            updateLastTaskId(epic.getId());
        }
        //Новый эпик не должен содержать подзадачи
        epic.removeAllSubtask();
        //Т. З. если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW
        epic.setStatus(TaskStatus.NEW);
        prioritizedTasks.add(epic);
        epicMap.put(epic.getId(), epic);
        return true;
    }

   @Override
   public boolean updateEpic(Epic epic) {
        if (epic == null) {
            return false;
        }
        Epic oldEpic = getEpicLocal(epic.getId());
       //Если эпик не существует нечего обновлять. Возврат ложь.
       if (oldEpic == null) {
           return false;
       }
       epic = new Epic(epic);
       prioritizedTasks.remove(oldEpic);
       prioritizedTasks.add(epic);
       epicMap.put(epic.getId(), epic);
       return  true;
   }

    private boolean updateEpicLocal(Epic epic) {
        Epic oldEpic = getEpicLocal(epic.getId());
        //Если эпик не существует нечего обновлять. Возврат ложь.
        if (oldEpic == null) {
            return false;
        }

        //Синхронизируем подзадачи с хранилищем менеджера
        List<Long> listSubtaskId = epic.getListSubtaskId();
        for (Long subtaskId : listSubtaskId) {
            if (getSubtaskLocal(subtaskId) == null) {
                epic.removeSubtask(subtaskId);
            }
        }

        List<Subtask>  subtaskList = getListSubtaskFromEpic(epic.getId());
        epic.setDuration(getSumDuration(subtaskList));
        epic.setStartTime(getMinStartTime(subtaskList));
        epic.setEndTime(getMaxEndTime(subtaskList));
        if (subtaskList.isEmpty() || allSubtasksWithStatusNew(subtaskList)) {
            epic.setStatus(TaskStatus.NEW); //нет подзадач или все они имеют статус NEW.
        } else if (allSubtasksWithStatusDone(subtaskList)) {
            epic.setStatus(TaskStatus.DONE); //все подзадачи имеют статус DONE
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        prioritizedTasks.remove(oldEpic);
        prioritizedTasks.add(epic);
        epicMap.put(epic.getId(), epic);
        return  true;
    }

    @Override
    public boolean removeEpic(Long id) {
        Epic epic = getEpicLocal(id);
        if (epic == null) {
            return false;
        }

        List<Long> listSubtaskId = epic.getListSubtaskId();
        for (Long subtaskId : listSubtaskId) {
            Task task = getTaskLocal(id);
            if (task != null) {
                prioritizedTasks.remove(task);
            }
            historyManager.remove(subtaskId);
            subtaskMap.remove(subtaskId);

        }
        prioritizedTasks.remove(epic);
        historyManager.remove(id);
        return epicMap.remove(id) != null;
    }

    @Override
    public List<Subtask> getListSubtaskFromEpic(Long id) {
        List<Subtask> subtaskList = new ArrayList<>();
        Epic epic = getEpicLocal(id);
        if (epic == null) {
            return subtaskList; //возврат пустого списка
        }
        List<Long> listSubtaskId = epic.getListSubtaskId();
        for (Long subtaskId : listSubtaskId) {
            Subtask subtask = getSubtaskLocal(subtaskId);
            if (subtask != null) {
                subtaskList.add(subtask);
            }
        }
        return subtaskList;
    }

    //Подзадачи------------------------------------------------------
    @Override
    public List<Subtask> getListSubtask() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public void removeAllSubtask() {
        //более короткое решение удалить всё сразу и обновить все эпике в hashmap
        //более правильное найти и обновить связанные эпики
        List<Epic> epics = new ArrayList<>();
        for (Subtask subtask : subtaskMap.values()) {
            Epic epic = getEpicLocal(subtask.getParentId());
            if (epic != null) {
                epics.add(epic);
            }
        }
        removePrioritizedTasks(subtaskMap);
        removeHistory(subtaskMap);
        subtaskMap.clear();
        for (Epic epic : epics) {
            updateEpicLocal(epic);
        }
    }

    @Override
    public Subtask getSubtask(Long id) {
        Subtask subtask = getSubtaskLocal(id);
        if (subtask == null) {
            return null;
        }
        historyManager.add(subtask);
        return subtask;
    }
    private Subtask getSubtaskLocal(Long id) {
        return subtaskMap.get(id);
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        if (subtask == null) return false;
        // Если подзадача существует нечего не создаем. Возврат ложь.
        if (getSubtaskLocal(subtask.getId()) != null)  {
            return false;
        }
        Epic epic = getEpicLocal(subtask.getParentId());
        //Если родитель не существует нечего создавать. Возврат ложь.
        if (epic == null) {
            return false;
        }
        //Локально копируем, чтобы не меняли снаружи
        subtask = new Subtask(subtask);
        if (subtask.getId() == null) {
            subtask.setId(calcNextTaskId());
        } else {
            updateLastTaskId(subtask.getId());
        }
        validationTask(subtask);
        epic.addSubtask(subtask.getId());
        prioritizedTasks.add(subtask);
        subtaskMap.put(subtask.getId(), subtask);
        //обновляем родителя
        updateEpicLocal(epic);
        return true;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (subtask == null) {
            return false;
        }
        Subtask oldSubtask = getSubtaskLocal(subtask.getId());
        // Если подзадача не существует, нечего не обновляем. Возврат ложь.
        if (oldSubtask == null)  {
            return false;
        }
        Epic epic = getEpicLocal(subtask.getParentId());
        //Если родитель не существует нечего обновлять. Возврат ложь.
        if (epic == null) {
            return false;
        }
        //Локально копируем, чтобы не меняли снаружи
        subtask = new Subtask(subtask);
        validationTask(subtask);
        epic.addSubtask(subtask.getId());
        prioritizedTasks.remove(oldSubtask);
        prioritizedTasks.add(subtask);
        subtaskMap.put(subtask.getId(), subtask);
        //обновляем родителя
        updateEpicLocal(epic);
        return true;
    }

    @Override
    public boolean removeSubtask(Long id) {
        Subtask subtask = getSubtaskLocal(id);
        if (subtask == null) {
            return  false;
        }
        Epic epic = getEpicLocal(subtask.getParentId());
        prioritizedTasks.remove(subtask);
        historyManager.remove(id);
        subtaskMap.remove(id);
        if (epic != null) {
            updateEpicLocal(epic);
        }
        return true;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private boolean allSubtasksWithStatusNew(List<Subtask> subtaskList) {
        for (Subtask subtask : subtaskList) {
            if (subtask.getStatus() != TaskStatus.NEW) {
                return false;
            }
        }
        return true;
    }

    private boolean allSubtasksWithStatusDone(List<Subtask> subtaskList) {
        for (Subtask subtask : subtaskList) {
            if (subtask.getStatus() != TaskStatus.DONE) {
                return false;
            }
        }
        return  true;
    }

    private void removeHistory(Map<Long, ? extends Task> map) {
        if (map == null) {
            return;
        }
        for (Task task : map.values()) {
            historyManager.remove(task.getId());
        }
    }

    private Integer getSumDuration(List<Subtask> subtaskList) {
        if (subtaskList.isEmpty()) {
            return null;
        }
        boolean notNull = false;
        Integer sumDuration = 0;
        for (Subtask subtask : subtaskList) {
            Integer duration = subtask.getDuration();
            if (duration != null) {
                sumDuration += duration;
                notNull = true;
            }
        }
        if (notNull) {
            return sumDuration;
        }
        return null;
    }

    private LocalDateTime getMaxEndTime(List<Subtask> subtaskList) {
        if (subtaskList.isEmpty()) {
            return null;
        }

        Subtask subtask = Collections.max(subtaskList, Comparator.comparing(Task::getEndTime,
                Comparator.nullsFirst(Comparator.naturalOrder())));
        if (subtask != null) {
            return subtask.getEndTime();
        }
        return null;
    }

    private LocalDateTime getMinStartTime(List<Subtask> subtaskList) {
        if (subtaskList.isEmpty()) {
            return null;
        }

        Subtask subtask = Collections.min(subtaskList,Comparator.comparing(Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())));
        if (subtask != null) {
            return subtask.getStartTime();
        }
        return null;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void removePrioritizedTasks(Map<Long, ? extends Task> map) {
        if (map == null) {
            return;
        }
        for (Task task : map.values()) {
            prioritizedTasks.remove(task);
        }
    }

    private void validationTask(Task task) {
        if (task.getStartTime() == null) {
            return;
        }
        List<Task> tasks = getPrioritizedTasks();
        for (Task element : tasks) {
            LocalDateTime startDate = task.getStartTime();
            LocalDateTime endDate = task.getEndTime();
            LocalDateTime elementStartDate = element.getStartTime();
            LocalDateTime elementEndDate = element.getEndTime();
            if (elementStartDate == null || elementEndDate == null || element.getType() == TaskType.EPIC) {
                continue; // Игнорируем если незакрытый интервал или Эпик
            }

            if (startDate.isAfter(elementStartDate) && startDate.isBefore(elementEndDate)) {
                throw new ManagerTaskValidationException("Дата начала пересекается");
            }
            if (endDate.isAfter(elementStartDate) && endDate.isBefore(elementEndDate)) {
                throw new ManagerTaskValidationException("Дата окончания пересекается");
            }
        }
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "taskMap=" + taskMap.keySet() +
                ", epicMap=" + epicMap.keySet() +
                ", subtaskMap=" + subtaskMap.keySet() +
                '}';
    }
}
