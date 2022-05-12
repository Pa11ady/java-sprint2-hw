package ru.yandex.practicum.tasktracker.manager;

import java.io.File;

public class Managers {
    // создание классов в одном экземпляре решает проблемы связывания HistoryManager и TaskManager
    // мне кажется каждый раз создавать новый объект HistoryManager нелогично

    final static  String PATH = "resources" + File.separator + "tasks_tmp.csv";

    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager taskManager = new FileBackedTasksManager(new File(PATH), historyManager);

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
