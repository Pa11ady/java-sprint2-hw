package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;
import ru.yandex.practicum.tasktracker.taskmanager.TaskManager;

public class Main {

    public static void main(String[] args) {
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

        System.out.println("====Проверка Создания задач====");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println("------");
        System.out.println("Subtask " + epic1.getListSubtaskId() );
        System.out.println("===============================");

        System.out.println("====Проверка работы менеджера====");

        //Задача
        testCreateDeleteTask();
        //Эпик

        //Подзадача

    }

    private static void testCreateDeleteTask() {
        long id = TaskManager.calcNextTaskId();
        Task task1 = new Task(id, "Задача " + id, "Важная задача " + id, TaskStatus.NEW);
        id = TaskManager.calcNextTaskId();
        Task task2 = new Task(id, "Задача " + id, "Интересная задача " + id, TaskStatus.NEW);
        id = TaskManager.calcNextTaskId();
        Task task3 = new Task(id, "Задача " + id, "Интересная задача " + id, TaskStatus.NEW);

        TaskManager taskManager = new TaskManager();
        System.out.println("Создание задач");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        System.out.println(taskManager);

        System.out.println("Удаление задачи");
        taskManager.removeTask(task1.getId());
        System.out.println(taskManager);

        System.out.println("Удаление задачи");
        taskManager.removeAllTask();
        System.out.println(taskManager);
    }
}