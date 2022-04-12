package ru.yandex.practicum.tasktracker.task;

public class Subtask extends Task {
    private final Long parentId;

    public Subtask(Long id, String name, String description, TaskStatus status, Long parentId) {
        super(id, name, description, status);
        this.parentId = parentId;
    }

    public Subtask(Subtask subtask) {
        this(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.parentId);
    }

    public Long getParentId() {
        return parentId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
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
