package ru.yandex.practicum.tasktracker.client;

import ru.yandex.practicum.tasktracker.exception.KVTaskClientGetTokenException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String URL;
    private final String apiToken;
    private final HttpClient httpClient;

    public KVTaskClient(String URL) throws IOException, InterruptedException {
        this.URL = URL;
        httpClient = HttpClient.newHttpClient();
        URI url = URI.create(URL + "/register");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new KVTaskClientGetTokenException("Не удалось получить токен");
        }
        apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException  {
        URI url = URI.create(URL + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI url = URI.create(URL + "/load/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
