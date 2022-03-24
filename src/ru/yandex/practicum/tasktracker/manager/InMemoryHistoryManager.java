package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryHistoryManager implements HistoryManager{
    private static class Node {
        final Task task;
        Node prev;
        Node next;

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;
    private final Set<Task> historySet = new HashSet<>();
    private final List<Task> historyOld = new ArrayList<>();
    public final int MAX_SIZE = 10;

    @Override
    public void add(Task task) {
        historyOld.add(task);
        if (historyOld.size() > MAX_SIZE) {
            historyOld.remove(0);
        }
    }

    @Override
    public List<Task> getHistoryOld() {
        // возврат неглубокой копии списка
        return new ArrayList<>(historyOld);
    }

    @Override
    public void remove(int id) {

    }

    //отладочный метод для тестов
    @Override
    public  void clearHistory() {
        historyOld.clear();
    }
}
