package ru.yandex.practicum.tasktracker.mainapp;

import com.google.gson.Gson;
import ru.yandex.practicum.tasktracker.client.KVTaskClient;
import ru.yandex.practicum.tasktracker.enums.TaskStatus;
import ru.yandex.practicum.tasktracker.manager.HttpTaskManager;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.server.HttpTaskServer;
import ru.yandex.practicum.tasktracker.server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    private static final String KV_URL = "http://localhost:8078";

    public static void main(String[] args) throws IOException, InterruptedException {
        testKvServ();
        testHttp1();
        testHttpTaskManager();
    }

    private static void testHttpTaskManager() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskManager taskManager = new HttpTaskManager(KV_URL);

        final LocalDateTime taskDate1 = LocalDateTime.of(2021, 2, 20, 10, 0);
        final LocalDateTime taskDate2 = LocalDateTime.of(2021, 2, 5, 15, 0);
        final LocalDateTime taskDate3 = LocalDateTime.of(2021, 2, 10, 15, 0);

        final LocalDateTime subtaskDate3 = LocalDateTime.of(2021, 1, 3, 10, 0);
        final Integer subtaskDuration3 = 120;

        final long EPIC_ID1 = 40L;
        final long EPIC_ID2 = 50L;
        final long EPIC_ID3 = 60L;

        final Task task1 = new Task("t1", "t1", TaskStatus.NEW, 120, taskDate1);
        final Task task2 = new Task("t2", "t2", TaskStatus.NEW);
        final Task task3 = new Task("t3", "t3", TaskStatus.NEW, 60, taskDate3);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        final Epic epic1 = new Epic(EPIC_ID1, "????????1", "???????? 1 ????????????????");
        final Epic epic2 = new Epic(EPIC_ID2, "????????2", "???????? 2 ????????????????");
        final Epic epic3 = new Epic(EPIC_ID3, "????????3", "???????? 3 ????????????????");

        final Subtask subtask1 = new Subtask("?????????????????? 1.1", "???????????????? 1", TaskStatus.NEW, EPIC_ID1);
        final Subtask subtask2 = new Subtask("?????????????????? 2.1", "???????????????? 2.1",
                TaskStatus.NEW, EPIC_ID2);
        final Subtask subtask3 = new Subtask( "?????????????????? 2.2", "???????????? 2.2", TaskStatus.NEW, EPIC_ID2,
                subtaskDuration3, subtaskDate3);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        taskManager.getEpic(EPIC_ID1);
        taskManager.getEpic(EPIC_ID3);

        HttpTaskManager httpTaskLoad = HttpTaskManager.loadFromUrl(KV_URL);
        printTask(httpTaskLoad.getListTask());
        printTask(httpTaskLoad.getListEpic());
        printTask(httpTaskLoad.getListSubtask());
        System.out.println("??????????????");
        printTask(httpTaskLoad.getHistory());
        System.out.println("*************************");
        printTask(httpTaskLoad.getPrioritizedTasks());


        taskManager.removeEpic(EPIC_ID1);
        taskManager.removeEpic(EPIC_ID2);
        taskManager.removeEpic(100500L);
        httpTaskLoad = HttpTaskManager.loadFromUrl(KV_URL);
        System.out.println("=======????????????????!!!===========");
        printTask(httpTaskLoad.getListTask());
        printTask(httpTaskLoad.getListEpic());
        printTask(httpTaskLoad.getListSubtask());
        System.out.println("??????????????");
        printTask(httpTaskLoad.getHistory());
        System.out.println("=======?????? ????????????!!!===========");
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
        httpTaskLoad = HttpTaskManager.loadFromUrl(KV_URL);
        printTask(httpTaskLoad.getListTask());
        printTask(httpTaskLoad.getListEpic());
        printTask(httpTaskLoad.getListSubtask());
        printTask(httpTaskLoad.getHistory());
        printTask(httpTaskLoad.getPrioritizedTasks());

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
        KVServer kvServer = new KVServer();
        kvServer.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        createTask(new Task(100L, "task100", "task100", TaskStatus.NEW));
        createTask(new Task(200L, "tas200", "task200", TaskStatus.NEW));
        createTask(new Task(200L, "tas300", "task300", TaskStatus.NEW));
        getAllTasks();
        System.out.println("********");
        getTask(100L);
        httpTaskServer.stop();
        kvServer.stop();
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

    private static void printTask(List<? extends Task> tasks) {
        System.out.println("==========");
        for (var el : tasks) {
            System.out.println(el);
        }
        System.out.println("==========");
    }
}