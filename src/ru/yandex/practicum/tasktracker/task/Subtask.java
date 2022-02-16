package ru.yandex.practicum.tasktracker.task;

public class Subtask extends Task {
    private long parentId;

    public Subtask(long id, String name, String description, TaskStatus status, long parentId) {
        super(id, name, description, status);
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "parentId=" + parentId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
