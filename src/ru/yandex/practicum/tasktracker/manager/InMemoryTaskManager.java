package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static Long calcNextTaskId() {
        return ++lastTaskId;
    }

    public InMemoryTaskManager() {
        //для совместимости со старыми тестами или кодом
        historyManager = Managers.getDefaultHistory();
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
        }
        taskMap.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean updateTask(Task task) {
        if (task == null) {
            return false;
        }
        // Если задача не существует нечего обновлять. Возврат ложь.
        if (getTaskLocal(task.getId()) == null) {
            return false;
        }
        // локально копируем, чтобы не меняли снаружи
        task = new Task(task);
        taskMap.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean removeTask(Long id) {
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
        //Если Эпик существует нечего не создаем. Возврат ложь.
        if (getEpicLocal(epic.getId()) != null)  {
            return false;
        }
        epic = new Epic(epic);
        if (epic.getId() == null) {
            epic.setId(calcNextTaskId());
        }
        //Новый эпик не должен содержать подзадачи
        epic.removeAllSubtask();
        //Т. З. если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW
        epic.setStatus(TaskStatus.NEW);
        epicMap.put(epic.getId(), epic);
        return true;
    }

   @Override
   public boolean updateEpic(Epic epic) {
       //Если эпик не существует нечего обновлять. Возврат ложь.
       if (getEpicLocal(epic.getId()) == null) {
           return false;
       }
       epic = new Epic(epic);
       epicMap.put(epic.getId(), epic);
       return  true;
   }

    private boolean updateEpicLocal(Epic epic) {
        //Если эпик не существует нечего обновлять. Возврат ложь.
        if (getEpicLocal(epic.getId()) == null) {
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
        if (subtaskList.isEmpty() || allSubtasksWithStatusNew(subtaskList)) {
            epic.setStatus(TaskStatus.NEW); //нет подзадач или все они имеют статус NEW.
        } else if (allSubtasksWithStatusDone(subtaskList)) {
            epic.setStatus(TaskStatus.DONE); //все подзадачи имеют статус DONE
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
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
            historyManager.remove(subtaskId);
            subtaskMap.remove(subtaskId);
        }
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
            subtask.setId(InMemoryTaskManager.calcNextTaskId());
        }
        epic.addSubtask(subtask.getId());
        subtaskMap.put(subtask.getId(), subtask);
        //обновляем родителя
        updateEpicLocal(epic);
        return true;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        // Если подзадача не существует, нечего не обновляем. Возврат ложь.
        if (getSubtaskLocal(subtask.getId()) == null)  {
            return false;
        }
        Epic epic = getEpicLocal(subtask.getParentId());
        //Если родитель не существует нечего обновлять. Возврат ложь.
        if (epic == null) {
            return false;
        }
        //Локально копируем, чтобы не меняли снаружи
        subtask = new Subtask(subtask);
        epic.addSubtask(subtask.getId());
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

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "taskMap=" + taskMap.keySet() +
                ", epicMap=" + epicMap.keySet() +
                ", subtaskMap=" + subtaskMap.keySet() +
                '}';
    }
}
