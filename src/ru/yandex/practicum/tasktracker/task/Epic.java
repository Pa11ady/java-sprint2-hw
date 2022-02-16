package ru.yandex.practicum.tasktracker.task;

import java.util.TreeSet;

public class Epic extends Task {
    private final TreeSet<Long> subTaskSet = new TreeSet<>();

    public Epic(long id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public void addSubTask(Long id) {
        subTaskSet.add(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskSet=" + subTaskSet.toString() +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
