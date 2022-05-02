package ru.yandex.practicum.tasktracker.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final Long parentId;

    public Subtask(String name, String description, TaskStatus status, Long parentId) {
        super(name, description, status);
        this.parentId = parentId;
    }

    public Subtask(Long id, String name, String description, TaskStatus status, Long parentId) {
        super(id, name, description, status);
        this.parentId = parentId;
    }

    public Subtask(String name, String description, TaskStatus status, Long parentId, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.parentId = parentId;
    }

//Duration duration, LocalDateTime startTime
public Subtask(Long id, String name, String description, TaskStatus status, Long parentId, Duration duration,
               LocalDateTime startTime) {
    super(id, name, description, status, duration, startTime);
    this.parentId = parentId;
}
    public Subtask(Subtask subtask) {
        this(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.parentId,
                subtask.getDuration(), subtask.getStartTime());
    }

    public Long getParentId() {
        return parentId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toStringCSV() {
        return String.join(",", super.toStringCSV(), parentId.toString());
    }

    @Override
    public String toString() {
        return  getType() +
                "{" +
                "parentId=" + parentId +
                ", id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
