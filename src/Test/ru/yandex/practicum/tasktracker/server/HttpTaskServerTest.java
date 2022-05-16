package ru.yandex.practicum.tasktracker.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.client.HttpClientTestTaskManager;
import ru.yandex.practicum.tasktracker.manager.FileBackedTasksManager;
import ru.yandex.practicum.tasktracker.manager.TaskManagerTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

//Тестирования эндпоинтов класса HttpTaskServer

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
}