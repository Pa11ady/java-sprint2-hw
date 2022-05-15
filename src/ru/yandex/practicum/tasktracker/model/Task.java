package ru.yandex.practicum.tasktracker.model;

import ru.yandex.practicum.tasktracker.enums.TaskStatus;
import ru.yandex.practicum.tasktracker.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

//Решил максимально сузить область видимости, чтобы наследование не нарушало инкапсуляцию
public class Task {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;

    private Integer duration;   //Минуты
    private LocalDateTime startTime;

    public Task(String name, String description, TaskStatus status) {
        this(null, name, description, status, null, null);
    }

    public Task(Long id, String name, String description, TaskStatus status) {
        this(id, name, description, status, null, null);
    }

    public Task(String name, String description, TaskStatus status, Integer duration, LocalDateTime startTime) {
        this(null, name, description, status, duration, startTime);
    }

    public Task(Long id, String name, String description, TaskStatus status, Integer duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Task task) {
        this(task.id, task.name, task.description, task.status, task.duration, task.startTime);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public  LocalDateTime getEndTime() {
        if (startTime == null || duration == null) {
            return  null;
        }

        return startTime.plusMinutes(duration);
    }

    public String toStringCSV() {
        String duration = this.duration != null ? this.duration.toString() : " ";
        String startTime = this.startTime != null ? this.startTime.toString() : " ";

        return String.join(",", id.toString(), getType().toString(), name, status.toString(), description,
                duration, startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
