package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;
import ru.yandex.practicum.tasktracker.taskmanager.TaskManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Пришло время практики!");

        Task task1 = new Task(TaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(TaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Epic epic1 = new Epic(TaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(TaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(5, "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubTask(subtask1.getId());

        Subtask subtask2 = new Subtask(6, "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubTask(subtask2.getId());

        Subtask subtask3 = new Subtask(6, "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubTask(subtask3.getId());

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);

        System.out.println("=====");
        System.out.println("Subtask " + epic1.getListSubtaskId() );
        System.out.println("=====");
        System.out.println(new Task(task1));
        System.out.println(new Subtask(subtask1));
        System.out.println(new Epic(epic1));
    }
}