package ru.yandex.practicum.tasktracker.taskmanager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;

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

    //Получение списка всех задач
    public List<Task> getListTask() {
        return null;
    }

    //Удаление всех задач
   public void removeAllTask() {

    }

    //Получение по идентификатору
    public Task getTask(long id) {
        return null;
    }

    //Создание. Сам объект должен передаваться в качестве параметра
    public void createTask(Task task) {

    }

    //Обновление. Новая версия объекта
    public void updateTask() {

    }

    //Удаление по идентификатору
    public void removeTask(long id) {

    }

   //Получение списка всех задач
    public List<Epic> getListEpic() {
        return null;
    }

    //Удаление всех задач
    public void removeAllEpic() {

    }

    //Получение по идентификатору
    public Epic getTaskEpic(long id) {
        return null;
    }

    //Создание. Сам объект должен передаваться в качестве параметра
    public void createEpic() {

    }

    //Обновление. Новая версия объекта
    public void updateEpic() {

    }

    //Удаление по идентификатору
    public void  removeEpic() {

    }

    //Получение списка всех задач
    public List<Subtask> getListSubtask() {
        return null;
    }

    //Удаление всех задач
    public void removeAllSubtask() {

    }

    //Получение по идентификатору
    public Subtask get(Long id) {
        return null;
    }

    //Создание. Сам объект должен передаваться в качестве параметра
    public void createSubtask() {

    }

    //Обновление. Новая версия объекта
    public void updateSubtask() {

    }

    //Удаление по идентификатору
    public void removeSubtask() {

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
