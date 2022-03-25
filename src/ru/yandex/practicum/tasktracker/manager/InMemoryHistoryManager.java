package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private final Map<Long, Node> historyMap = new HashMap<>();

    private void addLast(Task task) {
        final Node oldTail = tail;
        tail = new Node(task, oldTail, null);
        if (oldTail == null)
            head = tail;
        else
            oldTail.next = tail;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (head == tail) {         //один элемент
            head = tail = null;
        } else if (head == node) {  //элемент в начале
            head = node.next;
            head.prev = null;
        } else if (tail == node) {  //элемент в конце
            tail = node.prev;
            tail.next = null;
        } else {                    //элемент в середине
            Node nodePrev = node.prev;
            Node nodeNext = node.next;
            nodePrev.next = nodeNext;
            nodeNext.prev = nodePrev;
        }
        node.prev = null;
        node.next = null;
    }

    @Override
    public void add(Task task) {
        Node oldNode = historyMap.get(task.getId());
        removeNode(oldNode);
        addLast(task);
        //обновился tail после addLast, который кладём в historyMap
        historyMap.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>();
        Node nextNode = head;
        while (nextNode != null) {
            taskList.add(nextNode.task);
            nextNode = nextNode.next;
        }
        return taskList;
    }

    @Override
    public void remove(Long id) {
        removeNode(historyMap.remove(id));
    }

    //отладочный метод для тестов
    @Override
    public  void clearHistory() {
        for (Node node : historyMap.values()) {
            removeNode(node);
        }
        historyMap.clear();
    }
}
