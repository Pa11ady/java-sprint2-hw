package ru.yandex.practicum.tasktracker.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tasktracker.manager.FileBackedTasksManager;
import ru.yandex.practicum.tasktracker.manager.Managers;
import ru.yandex.practicum.tasktracker.manager.TaskManager;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    final static  String PATH = "resources" + File.separator + "tasks_tmp.csv";
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;
    //private final TaskManager taskManager = Managers.getDefault(); !!!!!
    private final TaskManager taskManager = FileBackedTasksManager.loadFromFile(new File(PATH));
    private final Gson gson = new Gson();

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/test", this::handleTest);
        httpServer.createContext("/tasks/task", this::handleTasksTask);
        httpServer.createContext("/tasks/subtask", this::handleTasksSubtask);
        httpServer.createContext("/tasks/epic", this::handleTasksEpic);
        httpServer.createContext("/tasks/subtask/epic", this::handleTasksSubtaskEpic);
        httpServer.createContext("/tasks/history", this::handleTasksHistory);
        httpServer.createContext("/tasks", this::handleTasks);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        httpServer.start();
    }

    private String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    private void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(DEFAULT_CHARSET);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.length);
        httpExchange.getResponseBody().write(response);
    }

    private Long readId(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null || !query.startsWith("id=")) {
            return null;
        }
        String id = query.substring("id=".length());
        if (id.isBlank()) {
            return null;
        }
        return Long.parseLong(id);
    }

    private void handleTest(HttpExchange httpExchange) throws IOException {
        System.out.println("handleTest");
        String response = "Привет, тестовый метод референс работает!";

        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }

    private void handleTasksTask(HttpExchange httpExchange) throws IOException {
        System.out.println("Задачи");
        Long id;

        try {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    id = readId(httpExchange);
                    if (id != null) {
                        Task task = taskManager.getTask(id);
                        if (task == null) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(httpExchange, gson.toJson(task));
                    } else {
                        List<Task> tasks = taskManager.getListTask();
                        if (tasks.isEmpty()) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(httpExchange, gson.toJson(tasks));
                    }
                    break;
                case "POST":
                    String body = readText(httpExchange);
                    if (body.isEmpty()) {
                        System.out.println("Пустое тело запроса");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    Task task = gson.fromJson(body, Task.class);
                    if (taskManager.createTask(task)) {
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (taskManager.updateTask(task)) {
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "DELETE":
                    id = readId(httpExchange);
                    if (id != null) {
                        if(!taskManager.removeTask(id)) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                    } else {
                        taskManager.removeAllTask();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void handleTasksSubtask(HttpExchange httpExchange) throws IOException {
        System.out.println("Подзадачи");
        Long id;

        try {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    id = readId(httpExchange);
                    if (id != null) {
                        Subtask subtask = taskManager.getSubtask(id);
                        if (subtask == null) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(httpExchange, gson.toJson(subtask));
                    } else {
                        List<Subtask> subtasks = taskManager.getListSubtask();
                        if (subtasks.isEmpty()) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(httpExchange, gson.toJson(subtasks));
                    }
                    break;
                case "POST":
                    String body = readText(httpExchange);
                    if (body.isEmpty()) {
                        System.out.println("Пустое тело запроса");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (taskManager.createSubtask(subtask)) {
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (taskManager.updateSubtask(subtask)) {
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "DELETE":
                    id = readId(httpExchange);
                    if (id != null) {
                        if(!taskManager.removeSubtask(id)) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                    } else {
                        taskManager.removeAllSubtask();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void handleTasksEpic(HttpExchange httpExchange) throws IOException {
        System.out.println("Эпики");
        Long id;

        try {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    id = readId(httpExchange);
                    if (id != null) {
                        Epic epic = taskManager.getEpic(id);
                        if (epic == null) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(httpExchange, gson.toJson(epic));
                    } else {
                        List<Epic> epics = taskManager.getListEpic();
                        if (epics.isEmpty()) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                        sendText(httpExchange, gson.toJson(epics));
                    }
                    break;
                case "POST":
                    String body = readText(httpExchange);
                    if (body.isEmpty()) {
                        System.out.println("Пустое тело запроса");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (taskManager.createEpic(epic)) {
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (taskManager.updateEpic(epic)) {
                        httpExchange.sendResponseHeaders(200, 0);
                    } else {
                        httpExchange.sendResponseHeaders(400, 0);
                    }
                    break;
                case "DELETE":
                    id = readId(httpExchange);
                    if (id != null) {
                        if(!taskManager.removeEpic(id)) {
                            httpExchange.sendResponseHeaders(404, 0);
                            return;
                        }
                    } else {
                        taskManager.removeAllEpic();
                    }
                    httpExchange.sendResponseHeaders(200, 0);
                    break;
                default:
                    httpExchange.sendResponseHeaders(405, 0);
            }
        } finally {
            httpExchange.close();
        }

    }

    private void handleTasksSubtaskEpic(HttpExchange httpExchange) throws IOException {
        System.out.println("Подзадачи из эпика");
        try {
            if (!httpExchange.getRequestMethod().equals("GET")) {
                httpExchange.sendResponseHeaders(405, 0);
                return;
            }
            Long id = readId(httpExchange);
            if (id == null) {
                System.out.println("Неверные параметры запроса");
                httpExchange.sendResponseHeaders(400, 0);
            }
            List<Subtask> subtasks = taskManager.getListSubtaskFromEpic(id);
            if (subtasks.isEmpty()) {
                httpExchange.sendResponseHeaders(404, 0);
                return;
            }
            sendText(httpExchange, gson.toJson(subtasks));
        } finally {
            httpExchange.close();
        }
    }

    private void sendTasks(HttpExchange httpExchange, List<Task> tasks) throws IOException {
        try {
            if (!"GET".equals(httpExchange.getRequestMethod())) {
                httpExchange.sendResponseHeaders(405, 0);
                return;
            }
            if (tasks.isEmpty()) {
                httpExchange.sendResponseHeaders(404, 0);
                return;
            }
            sendText(httpExchange, gson.toJson(tasks));
        } finally {
            httpExchange.close();
        }
    }

    private void handleTasksHistory(HttpExchange httpExchange) throws IOException {
        sendTasks(httpExchange, taskManager.getHistory());
    }

    private void handleTasks(HttpExchange httpExchange) throws IOException {
        sendTasks(httpExchange, taskManager.getPrioritizedTasks());
    }
}


