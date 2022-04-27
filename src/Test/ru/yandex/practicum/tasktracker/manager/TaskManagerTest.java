package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract  class TaskManagerTest <T extends TaskManager> {
    private T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    void setUp() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
    }

    @Test
    void getListTask() {
        assertTrue(true);
    }

    @Test
    void removeAllTask() {
    }

    @Test
    void getTask() {
    }

    @Test
    void createTask() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void removeTask() {
    }

    @Test
    void getListEpic() {
    }

    @Test
    void removeAllEpic() {
    }

    @Test
    void getEpic() {
    }

    @Test
    void createEpic() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void removeEpic() {
    }

    @Test
    void getListSubtaskFromEpic() {
    }

    @Test
    void getListSubtask() {
    }

    @Test
    void removeAllSubtask() {
    }

    @Test
    void getSubtask() {
    }

    @Test
    void createSubtask() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void removeSubtask() {
    }

    @Test
    void getHistory() {
    }
}