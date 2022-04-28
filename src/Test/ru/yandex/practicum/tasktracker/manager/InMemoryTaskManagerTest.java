package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>  {
    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @Test
    void calcNextTaskId() {
    }

}