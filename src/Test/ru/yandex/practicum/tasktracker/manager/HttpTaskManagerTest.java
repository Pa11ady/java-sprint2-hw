package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeAll;
import ru.yandex.practicum.tasktracker.server.KVServer;

import java.io.IOException;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    final static String KV_URL = "http://localhost:8078";
    public HttpTaskManagerTest() throws IOException, InterruptedException {
        super(new HttpTaskManager(KV_URL));
    }

   @BeforeAll
   static void startKVServer() throws IOException {
       new KVServer().start();
   }
}