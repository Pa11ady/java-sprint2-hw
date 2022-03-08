package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.manager.Managers;
import ru.yandex.practicum.tasktracker.manager.TaskManager;
import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;
import ru.yandex.practicum.tasktracker.manager.InMemoryTaskManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        testFinalSprint();  // Тест из Т. З.
    }

    private static void testFinalSprint() {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ");
        System.out.println("-------------------");
        System.out.println("\nСоздание задач...");
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task(0, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(0, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getListTask();
        System.out.println("Список задач: " + tasks);
        long idTask1 = tasks.get(0).getId();
        long idTask2 = tasks.get(1).getId();
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));

        System.out.println("\nСоздание эпиков...");
        Epic epic1 = new Epic(0, "Эпик Спринт 1", "Завершить спринт 1", TaskStatus.NEW);
        Epic epic2 = new Epic(0, "Эпик Спринт 2", "Доделать спринт 2", TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListEpic();
        long idEpic1 = epics.get(0).getId();
        long idEpic2 = epics.get(1).getId();
        System.out.println("Список эпиков: " + epics);
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));

        System.out.println("\nСоздание подзадач...");
        Subtask subtask1 = new Subtask(0, "Подзадача 1.1", "Выучить теорию 1", TaskStatus.NEW, idEpic1);
        Subtask subtask2 = new Subtask(0, "Подзадача 2.1", "просто подзадача 2.1", TaskStatus.NEW, idEpic2);
        Subtask subtask3 = new Subtask(0, "Подзадача 2.2", "Сдать проект 2.2", TaskStatus.NEW, idEpic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        List<Subtask> subtasks = taskManager.getListSubtask();
        long subtaskId1 = subtasks.get(0).getId();
        long subtaskId2 = subtasks.get(1).getId();
        long subtaskId3 = subtasks.get(2).getId();
        System.out.println("Список подзадач: " + subtasks);
        System.out.println("подзадача1 " + taskManager.getSubtask(subtaskId1));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtaskId2));
        System.out.println("подзадача3 " + taskManager.getSubtask(subtaskId3));
        System.out.println("Эпики: ");
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));

        System.out.println("\nИзменение задач");
        task1 = new Task(tasks.get(0));
        task2 = new Task(tasks.get(1));
        task1.setStatus(TaskStatus.DONE);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));

        System.out.println("\nИзменение подзадач");
        subtask1 = new Subtask(subtasks.get(0));
        subtask2 = new Subtask(subtasks.get(1));
        subtask3 = new Subtask(subtasks.get(2));
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.NEW);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);
        System.out.println("\nУдалить задачу");
        taskManager.removeTask(idTask1);
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));

        System.out.println("\nУдалить эпик");
        taskManager.removeEpic(idEpic1);
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));
        System.out.println("Список подзадач: " + subtasks);
        System.out.println("подзадача1 " + taskManager.getSubtask(subtask1.getId()));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtask2.getId()));
        System.out.println("подзадача3 " + taskManager.getSubtask(subtask3.getId()));
    }
}