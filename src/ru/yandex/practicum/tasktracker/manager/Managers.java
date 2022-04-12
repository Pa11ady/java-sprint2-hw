package ru.yandex.practicum.tasktracker.manager;

public class Managers {
    // создание классов в одном экземпляре решает проблемы связывания HistoryManager и TaskManager
    // мне кажется каждый раз создавать новый объект HistoryManager нелогично
    private static final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager taskManager = new InMemoryTaskManager(historyManager);

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return historyManager;
    }
}
