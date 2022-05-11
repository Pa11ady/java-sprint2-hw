package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.server.HttpTaskServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new HttpTaskServer().start();
    }
}