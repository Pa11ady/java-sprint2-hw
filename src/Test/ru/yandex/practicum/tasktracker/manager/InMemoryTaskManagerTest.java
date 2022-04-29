package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>  {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @Test
    void calcNextTaskId() {
        final String MSG = "ID не совпадают";
        Long startID = InMemoryTaskManager.calcNextTaskId();
        for (int i = 1; i <= 10; i++) {
            assertEquals(startID + i, InMemoryTaskManager.calcNextTaskId(), MSG);
        }
    }
}