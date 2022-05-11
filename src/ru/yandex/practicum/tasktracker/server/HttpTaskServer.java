package ru.yandex.practicum.tasktracker.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/test", this::handleTest);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        httpServer.start();
    }

    private void handleTest(HttpExchange httpExchange) throws IOException {
        System.out.println("handleTest");
        String response = "Привет, тестовый метод референс работает!";

        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "text/html; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes(DEFAULT_CHARSET));
        }
    }
}


