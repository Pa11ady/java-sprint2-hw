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
    public List<Task> getList() {
        return null;
    }

    //Удаление всех задач
    public  void removeALL() {

    }

    //Получение по идентификатору

    //Создание. Сам объект должен передаваться в качестве параметра

    //Обновление. Новая версия объекта

    //Удаление по идентификатору

    //Получение списка всех подзадач определённого эпика

}
