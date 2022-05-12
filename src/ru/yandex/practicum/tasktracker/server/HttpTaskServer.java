package ru.yandex.practicum.tasktracker.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tasktracker.manager.Managers;
import ru.yandex.practicum.tasktracker.manager.TaskManager;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;
    private final TaskManager taskManager = Managers.getDefault();

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
        System.out.println("handleTasksTask");
        String response = "handleTasksTask";
        //Headers headers = httpExchange.getResponseHeaders();
       // headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);

        //Get getTask; Get getTask id=
        //Post add update
        //Delete task id; All task

        String[] pathSplit = httpExchange.getRequestURI().getPath().split("/");
        System.out.println(Arrays.toString(pathSplit));

        String query = httpExchange.getRequestURI().getQuery();
        Long id = null;
        if (query != null) {
            id = Long.parseLong(query.substring("id=".length()));
        }
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                if (id != null) {
                    Task task = taskManager.getTask(id);
                    if (task == null) {
                        httpExchange.sendResponseHeaders(404, 0);
                        return;
                    }
                } else {
                    List<Task> tasks = taskManager.getListTask();
                    if (tasks.isEmpty()) {
                        httpExchange.sendResponseHeaders(404, 0);
                        return;
                    }
                }
                break;
            case "POST":
                break;
            case "DELETE":
                if (id != null) {
                    taskManager.removeTask(id);
                } else {
                    taskManager.removeAllTask();;
                }
                httpExchange.sendResponseHeaders(200, 0);
                break;
            default:
                httpExchange.sendResponseHeaders(405, 0);
        }

       /* httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }*/
    }

    private void handleTasksSubtask(HttpExchange httpExchange) throws IOException {
        String response = "handleTasksSubtask";

        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }

    private void handleTasksEpic(HttpExchange httpExchange) throws IOException {
        String response = "handleTasksEpic";

        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }

    private void handleTasksSubtaskEpic(HttpExchange httpExchange) throws IOException {
        String response = "handleTasksSubtaskEpic";

        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }

    private void handleTasksHistory(HttpExchange httpExchange) throws IOException {
        String response = "handleTasksHistory";

        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }

    private void handleTasks(HttpExchange httpExchange) throws IOException {
        String response = "handleTasks";

        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }
}


