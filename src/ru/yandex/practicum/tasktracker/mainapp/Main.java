package ru.yandex.practicum.tasktracker.mainapp;

import com.google.gson.Gson;
import ru.yandex.practicum.tasktracker.client.KVTaskClient;
import ru.yandex.practicum.tasktracker.enums.TaskStatus;
import ru.yandex.practicum.tasktracker.manager.HttpTaskManager;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.server.HttpTaskServer;
import ru.yandex.practicum.tasktracker.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private static final String KV_URL = "http://localhost:8078";

    public static void main(String[] args) throws IOException, InterruptedException {
        //testKvServ();
        //testHttp1();
        testHttpTaskManager();

    }

    private static void testHttpTaskManager() throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskManager httpTaskManager = new HttpTaskManager(KV_URL);
        HttpTaskManager.loadFromFile(null);
        kvServer.stop();

    }
    private static void testKvServ() throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        KVTaskClient kvTaskClient = new KVTaskClient(KV_URL);
        Gson gson = new Gson();

        Task task = new Task(100L, "task100", "task100", TaskStatus.NEW);
        kvTaskClient.put("123", gson.toJson(task));
        String taskJson = kvTaskClient.load("123");
        System.out.println(taskJson);
        kvServer.stop();
    }

    private static void testHttp1() throws IOException, InterruptedException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        createTask(new Task(100L, "task100", "task100", TaskStatus.NEW));
        createTask(new Task(200L, "tas200", "task200", TaskStatus.NEW));
        createTask(new Task(200L, "tas300", "task300", TaskStatus.NEW));
        getAllTasks();
        System.out.println("********");
        getTask(100L);
        httpTaskServer.stop();

    }

    private static void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Gson gson = new Gson();
        System.out.println("=======");
        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        for(var el : tasks) {
            System.out.println(el);
        }
    }

    private static void createTask(Task newTask) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        Gson gson = new Gson();
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void getTask(Long id) throws  IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Gson gson = new Gson();
        Task task = gson.fromJson(response.body(), Task.class);
        System.out.println(task);
    }
}