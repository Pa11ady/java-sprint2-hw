package ru.yandex.practicum.tasktracker.exception;

public class ManagerTaskValidationException extends RuntimeException{
    public ManagerTaskValidationException(final String message) {
        super(message);
    }
}