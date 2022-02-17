package ru.yandex.practicum.tasktracker.task;

public class Subtask extends Task {
    private final long parentId;

    public Subtask(long id, String name, String description, TaskStatus status, long parentId) {
        super(id, name, description, status);
        this.parentId = parentId;
    }

    public Subtask(Subtask subtask) {
        this(subtask.id, subtask.name, subtask.description, subtask.getStatus(), subtask.parentId);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "parentId=" + parentId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
