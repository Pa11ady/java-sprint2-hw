package ru.yandex.practicum.tasktracker.task;

//Решил максимально сузить область видимости, чтобы наследование не нарушало инкапсуляцию
public class Task {
    private long id;    //передать 0 для генерации уникального id
    private String name;
    private String description;
    private TaskStatus status;

    public Task(long id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Task task) {
        this(task.id, task.name, task.description, task.status);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
