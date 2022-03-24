package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistoryOld();

    void remove(int id);

    //отладочный метод
    void clearHistory();
}
