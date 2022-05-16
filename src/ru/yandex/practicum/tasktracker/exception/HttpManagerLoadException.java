package ru.yandex.practicum.tasktracker.exception;

public class HttpManagerLoadException extends RuntimeException {
    public HttpManagerLoadException(final String message) {
        super(message);
    }
}
