package ru.yandex.practicum.tasktracker.task;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Epic extends Task {
    private final TreeSet<Long> subtaskSet = new TreeSet<>();

    public Epic(long id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public void addSubTask(Long id) {
        subtaskSet.add(id);
    }

    public List<Long> getListSubtaskId() {
        return new ArrayList<>(subtaskSet);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskSet=" + subtaskSet +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
