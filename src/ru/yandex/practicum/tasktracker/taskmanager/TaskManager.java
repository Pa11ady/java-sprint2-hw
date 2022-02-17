package ru.yandex.practicum.tasktracker.taskmanager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;

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

    public List<Task> getListTask() {
        return new ArrayList<>(taskHashMap.values());
    }

   public void removeAllTask() {
        taskHashMap.clear();
    }

    public Task getTask(long id) {
        return taskHashMap.get(id);
    }

    public void createTask(Task task) {
        // локально копирум, чтобы не меняли снаружи
        task = new Task(task);
        if (task.getId() == 0) {
            task.setId(calcNextTaskId());
        }
        taskHashMap.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (taskHashMap.get(task.getId()) == null) {
            return;
        }
        createTask(task);
    }

    public void removeTask(long id) {
        taskHashMap.remove(id);
    }

    public List<Epic> getListEpic() {
        return null;
    }

    //Удаление всех задач
    public void removeAllEpic() {

    }

    //Получение по идентификатору
    public Epic getEpic(long id) {
        return null;
    }

    //Создание. Сам объект должен передаваться в качестве параметра
    public void createEpic(Epic epic) {

    }

    //Обновление. Новая версия объекта
    public void updateEpic(Epic epic) {

    }

    //Удаление по идентификатору
    public void  removeEpic(long id) {

    }

    //Получение списка всех задач
    public List<Subtask> getListSubtask() {
        return null;
    }

    //Удаление всех задач
    public void removeAllSubtask() {

    }

    //Получение по идентификатору
    public Subtask getSubtask(Long id) {
        return null;
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

    //Получение списка всех подзадач определённого эпика
    public List<Subtask> getListSubtaskFromEpic(long id) {
        return null;
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
