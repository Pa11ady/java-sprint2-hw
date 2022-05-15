package ru.yandex.practicum.tasktracker.manager;

import com.google.gson.Gson;
import ru.yandex.practicum.tasktracker.client.KVTaskClient;
import ru.yandex.practicum.tasktracker.exception.HttpManagerSaveException;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.server.HttpTaskServer;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final static String TASK_KEY = "TASK";
    private final static String EPIC_KEY = "EPIC";
    private final static String SUB_KEY = "SUBTASK";
    private final static String HISTORY_KEY = "HISTORY";

    private final KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();
    public HttpTaskManager(String url) throws IOException, InterruptedException {
        super(null);
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        System.out.println("save");
        List<Task> taskList = super.getListTask();
        List<Epic> epicList = super.getListEpic();
        List<Subtask> subtaskList = super.getListSubtask();
        List<Task> historyList = super.getHistory();

        Task[] tasks = taskList.toArray(new Task[taskList.size()]);
        Epic[] epics = epicList.toArray(new Epic[epicList.size()]);
        Subtask[] subtasks = subtaskList.toArray(new Subtask[taskList.size()]);
        Long[] historyKeys = new Long[historyList.size()];
        for (int i = 0; i< historyList.size(); i++) {
            historyKeys[i] = historyList.get(i).getId();
        }

        try {
            kvTaskClient.put(TASK_KEY, gson.toJson(tasks));
            kvTaskClient.put(EPIC_KEY, gson.toJson(epics));
            kvTaskClient.put(SUB_KEY, gson.toJson(subtasks));
            kvTaskClient.put(HISTORY_KEY, gson.toJson(historyKeys));
        } catch (IOException | InterruptedException e) {
            throw new HttpManagerSaveException("Невозможно сохранить HttpTaskManager");
        }





    }

    public static HttpTaskManager loadFromUrl(String url) {
        return null;
    }

    public static HttpTaskManager loadFromFile(File file) {
        throw new UnsupportedOperationException("Загрузка из файла для HttpTaskManager не поддерживается.");
    }
}
