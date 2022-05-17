package ru.yandex.practicum.tasktracker.client;

import com.google.gson.Gson;
import ru.yandex.practicum.tasktracker.exception.HttpClientTestTaskManagerException;
import ru.yandex.practicum.tasktracker.manager.TaskManager;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

//Web клиент для облегчения тестирования эндпоинтов класса HttpTaskServer

public class HttpClientTestTaskManager implements TaskManager {
    private final static String URL = "http://localhost:8080";
    private final static String ID = "?id=";
    private final static String TASK_H = "/tasks/task";
    private final static String SUBTASK_H = "/tasks/subtask";
    private final static String EPIC_H = "/tasks/epic";
    private final static String SUB_IN_EPIC_H = "/tasks/subtask/epic";
    private final static String HISTORY_H = "/tasks/history";
    private final static String PRIORITY_H = "/tasks";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private boolean removeData(String urlStr) {
        URI url = URI.create(urlStr);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientTestTaskManagerException("Не удалось удалить данные");
        }
        return response.statusCode() == 200;
    }

    @Override
    public List<Task> getListTask() {
        URI url = URI.create(URL + TASK_H);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientTestTaskManagerException("Не удалось получить список задач");
        }

        int statusCode = response.statusCode();
        String body = response.body();
        if (statusCode != 200 || body.isEmpty()) {
            return new ArrayList<>();
        }
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        return new ArrayList<>(Arrays.asList(tasks));
    }

    @Override
    public void removeAllTask() {
        removeData((URL + TASK_H));
    }

    @Override
    public Task getTask(Long id) {
        URI url = URI.create(URL + TASK_H + ID + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientTestTaskManagerException("Не удалось получить задачу");
        }

        int statusCode = response.statusCode();
        String body = response.body();
        if (statusCode != 200 || body.isEmpty()) {
            return null;
        }
        return gson.fromJson(response.body(), Task.class);
    }

    private String addTask(Task task) {
        if (task == null) {
            return  "";
        }
        URI url = URI.create(URL + TASK_H);
        String json = gson.toJson(task);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientTestTaskManagerException("Не удалось создать/обновить задачу");
        }
        if (response.statusCode() != 200 ) {
            return "";
        }
        return  response.body();
    }

    @Override
    public boolean createTask(Task task) {
        return "CREATE".equals(addTask(task));
    }

    @Override
    public boolean updateTask(Task task) {
        return "UPDATE".equals(addTask(task));
    }

    @Override
    public boolean removeTask(Long id) {
        if (id == null) {
            return false;
        }
        return removeData(URL + TASK_H + ID + id);
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
