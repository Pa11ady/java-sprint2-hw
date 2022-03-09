package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Осторожно в геттерах утекают наружу ссылки task, epic, subtask
//В ТЗ не сказан про это, так что решил не копировать ещё раз
public class InMemoryTaskManager implements TaskManager {
    private static long lastTaskId = 0;
    private final HashMap<Long, Task> taskHashMap = new HashMap<>();
    private final HashMap<Long, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Long, Subtask> subtaskHashMap = new HashMap<>();
    private final HistoryManager historyManager;

    public static long calcNextTaskId() {
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
        return new ArrayList<>(taskHashMap.values());
    }

    @Override
   public void removeAllTask() {
        taskHashMap.clear();
    }

    @Override
    public Task getTask(long id) {
        Task task = taskHashMap.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public boolean createTask(Task task) {
        // Если задача существует нечего не создаем. Возврат ложь.
        if (getTask(task.getId()) != null)  {
            return false;
        }
        // локально копируем, чтобы не меняли снаружи
        task = new Task(task);
        if (task.getId() == 0) {
            task.setId(calcNextTaskId());
        }
        taskHashMap.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean updateTask(Task task) {
        // Если задача не существует нечего обновлять. Возврат ложь.
        if (getTask(task.getId()) == null) {
            return false;
        }
        // локально копируем, чтобы не меняли снаружи
        task = new Task(task);
        taskHashMap.put(task.getId(), task);
        return true;
    }

    @Override
    public boolean removeTask(long id) {
        return taskHashMap.remove(id) != null;
    }

    //Эпики----------------------------------------------------------
    @Override
    public List<Epic> getListEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    @Override
    public void removeAllEpic() {
        epicHashMap.clear();
        subtaskHashMap.clear();
    }

    @Override
    public Epic getEpic(long id) {
        Epic epic = epicHashMap.get(id);
        if (epic == null) {
            return null;
        }
        historyManager.add(epic);
        return epic;
    }

    @Override
    public boolean createEpic(Epic epic) {
        //Если Эпик существует нечего не создаем. Возврат ложь.
        if (getEpic(epic.getId()) != null)  {
            return false;
        }
        epic = new Epic(epic);
        if (epic.getId() == 0) {
            epic.setId(calcNextTaskId());
        }
        //Новый эпик не должен содержать подзадачи
        epic.removeAllSubtask();
        //Т. З. если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW
        epic.setStatus(TaskStatus.NEW);
        epicHashMap.put(epic.getId(), epic);
        return true;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        //Если эпик не существует нечего обновлять. Возврат ложь.
        if (getEpic(epic.getId()) == null) {
            return false;
        }
        //Локально копируем, чтобы не меняли снаружи
        epic = new Epic(epic);
        //Синхронизируем подзадачи с хранилищем менеджера
        List<Long> listSubtaskId = epic.getListSubtaskId();
        for (long subtaskId : listSubtaskId) {
            if (getSubtask(subtaskId) == null) {
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
        epicHashMap.put(epic.getId(), epic);
        return  true;
    }

    @Override
    public boolean  removeEpic(long id) {
        Epic epic = getEpic(id);
        if (epic == null) {
            return false;
        }
        List<Long> listSubtaskId = epic.getListSubtaskId();
        for (long subtaskId : listSubtaskId) {
            subtaskHashMap.remove(subtaskId);
        }
        return epicHashMap.remove(id) != null;
    }

    @Override
    public List<Subtask> getListSubtaskFromEpic(long id) {
        List<Subtask> subtaskList = new ArrayList<>();
        Epic epic = getEpic(id);
        if (epic == null) {
            return subtaskList; //возврат пустого списка
        }
        List<Long> listSubtaskId = epic.getListSubtaskId();
        for (long subtaskId : listSubtaskId) {
            Subtask subtask = getSubtask(subtaskId);
            if (subtask != null) {
                subtaskList.add(subtask);
            }
        }
        return subtaskList;
    }

    //Подзадачи------------------------------------------------------
    @Override
    public List<Subtask> getListSubtask() {
        return new ArrayList<>(subtaskHashMap.values());
    }

    @Override
    public void removeAllSubtask() {
        //более короткое решение удалить всё сразу и обновить все эпике в hashmap
        //более правильное найти и обновить связанные эпики
        List<Epic> epics = new ArrayList<>();
        for (Subtask subtask : subtaskHashMap.values()) {
            Epic epic = getEpic(subtask.getParentId());
            if (epic != null) {
                epics.add(epic);
            }
        }
        subtaskHashMap.clear();
        for (Epic epic : epics) {
            updateEpic(epic);
        }
    }

    @Override
    public Subtask getSubtask(Long id) {
        Subtask subtask = subtaskHashMap.get(id);
        if (subtask == null) {
            return null;
        }
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        // Если подзадача существует нечего не создаем. Возврат ложь.
        if (getSubtask(subtask.getId()) != null)  {
            return false;
        }
        Epic epic = getEpic(subtask.getParentId());
        //Если родитель не существует нечего создавать. Возврат ложь.
        if (epic == null) {
            return false;
        }
        //Локально копируем, чтобы не меняли снаружи
        subtask = new Subtask(subtask);
        if (subtask.getId() == 0) {
            subtask.setId(InMemoryTaskManager.calcNextTaskId());
        }
        epic.addSubtask(subtask.getId());
        subtaskHashMap.put(subtask.getId(), subtask);
        //обновляем родителя
        updateEpic(epic);
        return true;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        // Если подзадача не существует, нечего не обновляем. Возврат ложь.
        if (getSubtask(subtask.getId()) == null)  {
            return false;
        }
        Epic epic = getEpic(subtask.getParentId());
        //Если родитель не существует нечего обновлять. Возврат ложь.
        if (epic == null) {
            return false;
        }
        //Локально копируем, чтобы не меняли снаружи
        subtask = new Subtask(subtask);
        epic.addSubtask(subtask.getId());
        subtaskHashMap.put(subtask.getId(), subtask);
        //обновляем родителя
        updateEpic(epic);
        return true;
    }

    @Override
    public boolean removeSubtask(long id) {
        Subtask subtask = getSubtask(id);
        if (subtask == null) {
            return  false;
        }
        Epic epic = getEpic(subtask.getParentId());
        //Смысла не было особого в проверке, но она была скорее код был боле похожий в проекте
        subtaskHashMap.remove(id);
        if (epic != null) {
            updateEpic(epic);
        }
        return true;
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

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "taskHashMap=" + taskHashMap.keySet() +
                ", epicHashMap=" + epicHashMap.keySet() +
                ", subtaskHashMap=" + subtaskHashMap.keySet() +
                '}';
    }
}
