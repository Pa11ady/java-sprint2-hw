package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> history = new ArrayList<>();
    public final int MAX_SIZE = 10;

    @Override
    public void add(Task task) {
        history.add(task);
        if (history.size() > MAX_SIZE) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        // возврат неглубокой копии списка
        return new ArrayList<>(history);
    }

    //отладочный метод для тестов
    @Override
    public  void clearHistory() {
        history.clear();
    }
}
