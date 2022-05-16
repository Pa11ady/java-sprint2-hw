package ru.yandex.practicum.tasktracker.manager;

import java.io.File;
import java.io.IOException;

public class Managers {
    // создание классов в одном экземпляре решает проблемы связывания HistoryManager и TaskManager
    // мне кажется каждый раз создавать новый объект HistoryManager нелогично

    final static  String PATH = "resources" + File.separator + "tasks_tmp.csv";
    private static final String KV_URL = "http://localhost:8078";

    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager taskManager = new FileBackedTasksManager(new File(PATH), historyManager);
    private static TaskManager taskManager1 = new HttpTaskManager(KV_URL);

    public static TaskManager getDefault() {
        return taskManager1;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
