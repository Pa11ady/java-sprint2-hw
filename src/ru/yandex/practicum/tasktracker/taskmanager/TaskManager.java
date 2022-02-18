package ru.yandex.practicum.tasktracker.taskmanager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private static long lastTaskId = 0;
    private HashMap<Long, Task> taskHashMap = new HashMap<>();
    private HashMap<Long, Epic> epicHashMap = new HashMap<>();
    private HashMap<Long, Subtask> subtaskHashMap = new HashMap<>();

    public static long calcNextTaskId() {
        return ++lastTaskId;
    }

    //Задачи---------------------------------------------------------
    public List<Task> getListTask() {
        return new ArrayList<>(taskHashMap.values());
    }

   public void removeAllTask() {
        taskHashMap.clear();
    }

    public Task getTask(long id) {
        return taskHashMap.get(id);
    }

    public boolean createTask(Task task) {
        // Если задача существует нечего не создаем. Возврат ложь.
        if (taskHashMap.get(task.getId()) != null)  {
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

    public boolean updateTask(Task task) {
        // Если задача не существует нечего обновлять. Возврат ложь.
        if (taskHashMap.get(task.getId()) == null) {
            return false;
        }
        // локально копируем, чтобы не меняли снаружи
        task = new Task(task);
        taskHashMap.put(task.getId(), task);
        return true;
    }

    public boolean removeTask(long id) {
        if (taskHashMap.remove(id) == null) {
            return false;
        }
        return  true;
    }

    //Эпики----------------------------------------------------------
    public List<Epic> getListEpic() {
        return new ArrayList<>(epicHashMap.values());
    }

    public void removeAllEpic() {
        epicHashMap.clear();
    }

    public Epic getEpic(long id) {
        return epicHashMap.get(id);
    }

    public boolean createEpic(Epic epic) {
        //Если Эпик существует нечего не создаем. Возврат ложь.
        if (epicHashMap.get(epic.getId()) != null)  {
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

    //Обновление. Новая версия объекта
    public void updateEpic(Epic epic) {

    }

    public boolean  removeEpic(long id) {
        if (epicHashMap.remove(id) == null) {
            return false;
        }
        return  true;
    }

    public List<Subtask> getListSubtaskFromEpic(long id) {
        List<Subtask> subtaskList = new ArrayList<>();
        Epic epic = getEpic(id);
        if (epic == null) {
            return subtaskList; //возврат пустого списка
        }
        List<Long> listSubtaskId = epic.getListSubtaskId();
        for (long  subtaskId : listSubtaskId) {
            Subtask subtask = getSubtask(subtaskId);
            if (subtask != null) {
                subtaskList.add(subtask);
            }
        }
        return subtaskList;
    }

    //Получение списка всех задач
    //Подзадачи------------------------------------------------------
    public List<Subtask> getListSubtask() {
        return null;
    }

    //Удаление всех задач
    public void removeAllSubtask() {

    }

    public Subtask getSubtask(Long id) {
        return subtaskHashMap.get(id);
    }

    //Создание. Сам объект должен передаваться в качестве параметра
    public void createSubtask(Subtask subtask) {

    }

    //Обновление. Новая версия объекта
    public void updateSubtask(Subtask subtask) {

    }

    //Удаление по идентификатору
    public void removeSubtask(long id) {

    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "taskHashMap=" + taskHashMap.keySet() +
                ", epicHashMap=" + epicHashMap.keySet() +
                ", subtaskHashMap=" + subtaskHashMap.keySet() +
                '}';
    }
}
