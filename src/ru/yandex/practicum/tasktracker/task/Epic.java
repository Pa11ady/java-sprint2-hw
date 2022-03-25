package ru.yandex.practicum.tasktracker.task;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Epic extends Task {
    private final Set<Long> subtaskSet = new HashSet<>();

    public Epic(Long id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public Epic(Epic epic) {
        this(epic.getId(), epic.getName(), epic.getDescription(), epic.getStatus());
        subtaskSet.addAll(epic.subtaskSet);
    }

    public void addSubtask(Long id) {
        subtaskSet.add(id);
    }

    public void removeAllSubtask() {
        subtaskSet.clear();
    }

    public boolean removeSubtask(Long id) {
        return subtaskSet.remove(id);
    }

    public List<Long> getListSubtaskId() {
        return new ArrayList<>(subtaskSet);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskSet=" + subtaskSet +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
