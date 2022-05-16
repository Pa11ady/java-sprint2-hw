package ru.yandex.practicum.tasktracker.client;

import ru.yandex.practicum.tasktracker.manager.TaskManager;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.net.http.HttpClient;
import java.util.List;

//Web клиент для облегчения тестирования энпоинтов класса HttpTaskServer

public class HttpClientTestTaskManagerServ implements TaskManager {
    private final static String URL = "http://localhost:8080";
    private final static String TASK_H = "/tasks/task";
    private final static String SUBTASK_H = "/tasks/subtask";
    private final static String EPIC_H = "/tasks/epic";
    private final static String SUB_IN_EPIC_H = "/tasks/subtask/epic";
    private final static String HISTORY_H = "/tasks/history";
    private final static String PRIORITY_H = "/tasks";


    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public List<Task> getListTask() {
        return null;
    }

    @Override
    public void removeAllTask() {

    }

    @Override
    public Task getTask(Long id) {
        return null;
    }

    @Override
    public boolean createTask(Task task) {
        return false;
    }

    @Override
    public boolean updateTask(Task task) {
        return false;
    }

    @Override
    public boolean removeTask(Long id) {
        return false;
    }

    @Override
    public List<Epic> getListEpic() {
        return null;
    }

    @Override
    public void removeAllEpic() {

    }

    @Override
    public Epic getEpic(Long id) {
        return null;
    }

    @Override
    public boolean createEpic(Epic epic) {
        return false;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        return false;
    }

    @Override
    public boolean removeEpic(Long id) {
        return false;
    }

    @Override
    public List<Subtask> getListSubtaskFromEpic(Long id) {
        return null;
    }

    @Override
    public List<Subtask> getListSubtask() {
        return null;
    }

    @Override
    public void removeAllSubtask() {

    }

    @Override
    public Subtask getSubtask(Long id) {
        return null;
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        return false;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        return false;
    }

    @Override
    public boolean removeSubtask(Long id) {
        return false;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return null;
    }

    @Override
    public List<Task> getHistory() {
        return null;
    }
}
