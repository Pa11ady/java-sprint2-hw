package ru.yandex.practicum.tasktracker.exception;

public class HttpManagerSaveException extends RuntimeException {
    public HttpManagerSaveException(final String message) {
        super(message);
    }
}
