package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>  {
    static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    public InMemoryTaskManagerTest() {
        super(taskManager);
    }

    @Test
    void calcNextTaskId() {
    }

}