package ru.yandex.practicum.tasktracker.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.enums.TaskType;
import ru.yandex.practicum.tasktracker.exception.HttpClientTestTaskManagerException;
import ru.yandex.practicum.tasktracker.manager.TaskManager;
import ru.yandex.practicum.tasktracker.manager.TaskManagerTest;
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
import java.util.List;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

//Тестирования эндпоинтов класса HttpTaskServer

//Нам это класс нужен, так как иначе не может использовать abstract public class TaskManagerTest <T extends TaskManager>
//Вызываем косвенно все эндпоинты HttpTaskServer через HttpClientTestTaskManager

class HttpTaskServerTest extends TaskManagerTest<HttpClientTestTaskManager> {
    public HttpTaskServerTest() {
        super(new HttpClientTestTaskManager());
    }

    @BeforeAll
    static void startKVServer() throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }

    @BeforeEach
    void setUp() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
    }

    @Test
    void testValidationTask() {
        //Пропускаем для HttpClientTestTaskManager, так серверное исключение не дойдёт до клиента
        //И чтобы было видно, что тест пропущен в журнале
        assumeTrue(false);
    }
}

//Web клиент для облегчения тестирования эндпоинтов класса HttpTaskServer

class HttpClientTestTaskManager implements TaskManager {
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

    private List<Task> parseJson(String json) {
        List<Task> tasks = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        for (var element : jsonArray) {
            Task task = null;
            System.out.println(element.toString());
            String type = element.getAsJsonObject().get("type").getAsString();
            switch (TaskType.valueOf(type)) {
                case TASK:
                    task = gson.fromJson(element, Task.class);
                    break;
                case SUBTASK:
                    task = gson.fromJson(element, Subtask.class);
                    break;
                case EPIC:
                    task = gson.fromJson(element, Epic.class);
                    break;
            }
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private boolean addData(String strUrl, String text) {
        URI url = URI.create(strUrl);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(text);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientTestTaskManagerException("Не удалось создать/обновить по " + strUrl);
        }
        return response.statusCode() == 200;
    }

    private boolean removeData(String strUrl) {
        URI url = URI.create(strUrl);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientTestTaskManagerException("Не удалось удалить данные по" + strUrl);
        }
        return response.statusCode() == 200;
    }

    private String getData(String strURL) {
        URI url = URI.create(strURL);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new HttpClientTestTaskManagerException("Не удалось получить данные по" + strURL);
        }

        if (response.statusCode() != 200) {
            return "";
        }
        return response.body();
    }

    private List<Task> getListTask(String strURL) {
        String json = getData(strURL);
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        return parseJson(json);
    }

    @Override
    public List<Task> getListTask() {
        return getListTask(URL + TASK_H);
    }

    @Override
    public void removeAllTask() {
        removeData((URL + TASK_H));
    }

    @Override
    public Task getTask(Long id) {
        if (id == null) {
            return null;
        }
        String json = getData(URL + TASK_H + ID + id);
        if (json.isEmpty()) {
            return null;
        }
        return gson.fromJson(json, Task.class);
    }

    @Override
    public boolean createTask(Task task) {
        if (task == null || getTask(task.getId()) != null) {
            return false;
        }
        return addData(URL + TASK_H, gson.toJson(task));
    }

    @Override
    public boolean updateTask(Task task) {
        if (task == null || getTask(task.getId()) == null) {
            return false;
        }
        return addData(URL + TASK_H, gson.toJson(task));
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
        String json = getData(URL + EPIC_H);
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Epic[] epics = gson.fromJson(json, Epic[].class);
        return new ArrayList<>(Arrays.asList(epics));
    }

    @Override
    public void removeAllEpic() {
        removeData((URL + EPIC_H));
    }

    @Override
    public Epic getEpic(Long id) {
        if (id == null) {
            return null;
        }
        String json = getData(URL + EPIC_H + ID + id);
        if (json.isEmpty()) {
            return null;
        }
        return gson.fromJson(json, Epic.class);
    }

    @Override
    public boolean createEpic(Epic epic) {
        if (epic == null || getEpic(epic.getId()) != null) {
            return false;
        }
        return addData(URL + EPIC_H, gson.toJson(epic));
    }

    @Override
    public boolean updateEpic(Epic epic) {
        if (epic == null || getEpic(epic.getId()) == null) {
            return false;
        }
        return addData(URL + EPIC_H, gson.toJson(epic));
    }

    @Override
    public boolean removeEpic(Long id) {
        if (id == null) {
            return false;
        }
        return removeData(URL + EPIC_H + ID + id);
    }

    @Override
    public List<Subtask> getListSubtaskFromEpic(Long id) {
        if (id == null) {
            return new ArrayList<>();
        }
        String json = getData(URL + SUB_IN_EPIC_H + ID + id);
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Subtask[] subtasks = gson.fromJson(json, Subtask[].class);
        return new ArrayList<>(Arrays.asList(subtasks));
    }

    @Override
    public List<Subtask> getListSubtask() {
        String json = getData(URL + SUBTASK_H);
        if (json.isEmpty()) {
            return new ArrayList<>();
        }
        Subtask[] subtasks = gson.fromJson(json, Subtask[].class);
        return new ArrayList<>(Arrays.asList(subtasks));
    }

    @Override
    public void removeAllSubtask() {
        removeData((URL + SUBTASK_H));
    }

    @Override
    public Subtask getSubtask(Long id) {
        if (id == null) {
            return null;
        }
        String json = getData(URL + SUBTASK_H + ID + id);
        if (json.isEmpty()) {
            return null;
        }
        return gson.fromJson(json, Subtask.class);
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        if (subtask == null || getSubtask(subtask.getId()) != null) {
            return false;
        }
        return addData(URL + SUBTASK_H, gson.toJson(subtask));
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        if (subtask == null || getSubtask(subtask.getId()) == null) {
            return false;
        }
        return addData(URL + SUBTASK_H, gson.toJson(subtask));
    }

    @Override
    public boolean removeSubtask(Long id) {
        if (id == null) {
            return false;
        }
        return removeData(URL + SUBTASK_H + ID + id);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return getListTask(URL + PRIORITY_H);
    }

    @Override
    public List<Task> getHistory() {
        return getListTask(URL + HISTORY_H);
    }
}