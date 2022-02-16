package ru.yandex.practicum.tasktracker.taskmanager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;

import java.util.HashMap;

public class TaskManager {
    private static long lastTaskId = 0;
    private HashMap<Long, Epic> epicHashMap = new HashMap<>();
    private HashMap<Long, Subtask> subtaskHashMap = new HashMap<>();
    private HashMap<Long, Task> taskHashMap = new HashMap<>();

    public static long calcNextTaskId() {
        return ++lastTaskId;
    }

    
}
