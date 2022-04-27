package ru.yandex.practicum.tasktracker.manager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager>  {
    static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    public InMemoryTaskManagerTest() {
        super(taskManager);
    }
}