package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Task;

public interface HistoryManager {
    void add(Task task);

    Task getHistory();
}
