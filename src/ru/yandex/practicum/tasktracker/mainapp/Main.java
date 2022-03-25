package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.manager.HistoryManager;
import ru.yandex.practicum.tasktracker.manager.Managers;
import ru.yandex.practicum.tasktracker.manager.TaskManager;
import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        testRemoveALL();
        testFinalSprint2();  // Тест из Т. З. спринт 2
        testFinalSprint3();  // Тест из Т. З. спринт 3
        testFinalSprint4();  // Тест из Т. З. спринт 4
    }

    private static void testFinalSprint2() {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ 2");
        System.out.println("-------------------");
        System.out.println("\nСоздание задач...");
        TaskManager taskManager = Managers.getDefault();
        taskManager.removeAllEpic();
        taskManager.removeAllTask();
        Task task1 = new Task(null, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(null, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getListTask();
        System.out.println("Список задач: " + tasks);
        long idTask1 = tasks.get(0).getId();
        long idTask2 = tasks.get(1).getId();
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));

        System.out.println("\nСоздание эпиков...");
        Epic epic1 = new Epic(null, "Эпик Спринт 1", "Завершить спринт 1", TaskStatus.NEW);
        Epic epic2 = new Epic(null, "Эпик Спринт 2", "Доделать спринт 2", TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListEpic();
        long idEpic1 = epics.get(0).getId();
        long idEpic2 = epics.get(1).getId();
        System.out.println("Список эпиков: " + epics);
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));

        System.out.println("\nИзменить эпик...");
        Epic tmpEpic = taskManager.getEpic(idEpic2);
        tmpEpic.setName("Новый эпик 2");
        tmpEpic.setDescription("Описание эпика 2");
        taskManager.updateEpic(tmpEpic);
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));

        System.out.println("\nСоздание подзадач...");
        Subtask subtask1 = new Subtask(null, "Подзадача 1.1", "Выучить теорию 1", TaskStatus.NEW, idEpic1);
        Subtask subtask2 = new Subtask(null, "Подзадача 2.1", "просто подзадача 2.1", TaskStatus.NEW, idEpic2);
        Subtask subtask3 = new Subtask(null, "Подзадача 2.2", "Сдать проект 2.2", TaskStatus.NEW, idEpic2);
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
        subtask3.setStatus(TaskStatus.DONE);
        subtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);
        System.out.println("подзадача1 " + taskManager.getSubtask(subtask1.getId()));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtask2.getId()));
        System.out.println("подзадача3 " + taskManager.getSubtask(subtask3.getId()));

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

    private static void testFinalSprint3() {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ 3");
        System.out.println("-------------------");
        System.out.println("\nСоздание задач...");
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.clearHistory(); //отладочный метод
        taskManager.removeAllEpic();
        taskManager.removeAllTask();
        printHistory(historyManager.getHistory());

        Task task1 = new Task(null, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(null, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getListTask();
        System.out.println("\nСписок задач: " + tasks);
        long idTask1 = tasks.get(0).getId();
        long idTask2 = tasks.get(1).getId();
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));
        printHistory(historyManager.getHistory());

        System.out.println("\nСоздание эпиков...");
        Epic epic1 = new Epic(null, "Эпик Спринт 1", "Завершить спринт 1", TaskStatus.NEW);
        Epic epic2 = new Epic(null, "Эпик Спринт 2", "Доделать спринт 2", TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListEpic();
        long idEpic1 = epics.get(0).getId();
        long idEpic2 = epics.get(1).getId();
        System.out.println("\nСписок эпиков: " + epics);
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));
        printHistory(historyManager.getHistory());

        System.out.println("\nСоздание подзадач...");
        Subtask subtask1 = new Subtask(null, "Подзадача 1.1", "Выучить теорию 1", TaskStatus.NEW, idEpic1);
        Subtask subtask2 = new Subtask(null, "Подзадача 2.1", "просто подзадача 2.1", TaskStatus.NEW, idEpic2);
        Subtask subtask3 = new Subtask(null, "Подзадача 2.2", "Сдать проект 2.2", TaskStatus.NEW, idEpic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        List<Subtask> subtasks = taskManager.getListSubtask();
        long subtaskId1 = subtasks.get(0).getId();
        long subtaskId2 = subtasks.get(1).getId();
        long subtaskId3 = subtasks.get(2).getId();
        System.out.println("\nСписок подзадач: " + subtasks);
        System.out.println("подзадача1 " + taskManager.getSubtask(subtaskId1));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtaskId2));
        System.out.println("подзадача3 " + taskManager.getSubtask(subtaskId3));
        System.out.println("Эпики: ");
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));
        printHistory(historyManager.getHistory());
        System.out.println("================");
        taskManager.getSubtask(subtaskId1);
        taskManager.getSubtask(subtaskId1);
        printHistory(historyManager.getHistory());
        for (int i = 0; i < 20; i++) {
            taskManager.getEpic(idEpic1);
        }
        printHistory(taskManager.getHistory());
    }

    private static void testFinalSprint4() {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ 4");
        System.out.println("-------------------");
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.clearHistory(); //отладочный метод
        taskManager.removeAllEpic();
        taskManager.removeAllTask();

        System.out.println("\nСоздание задач...");
        Task task1 = new Task(null, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(null, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        System.out.println("\nСоздание эпиков...");
        Epic epic1 = new Epic(null, "Эпик Спринт 1", "Завершить спринт 1", TaskStatus.NEW);
        Epic epic2 = new Epic(null, "Эпик Спринт 2", "Доделать спринт 2", TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListEpic();
        long idEpic1 = epics.get(0).getId();
        long idEpic2 = epics.get(1).getId();

        System.out.println("\nСоздание подзадач...");
        Subtask subtask1 = new Subtask(null, "Подзадача 1.1", "Выучить теорию 1", TaskStatus.NEW, idEpic1);
        Subtask subtask2 = new Subtask(null, "Подзадача 1.2", "просто подзадача 1.1", TaskStatus.NEW, idEpic1);
        Subtask subtask3 = new Subtask(null, "Подзадача 1.3", "Сдать проект 1.3", TaskStatus.NEW, idEpic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        printHistory(taskManager.getHistory());

        List<Task> tasks = taskManager.getListTask();
        System.out.println("\nСписок задач: " + tasks);
        long idTask1 = tasks.get(0).getId();
        long idTask2 = tasks.get(1).getId();
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("\nСписок эпиков: " + epics);
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        List<Subtask> subtasks = taskManager.getListSubtask();
        long subtaskId1 = subtasks.get(0).getId();
        long subtaskId2 = subtasks.get(1).getId();
        long subtaskId3 = subtasks.get(2).getId();
        System.out.println("\nСписок подзадач: " + subtasks);
        System.out.println("подзадача1 " + taskManager.getSubtask(subtaskId1));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtaskId2));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtaskId2));
        System.out.println("подзадача3 " + taskManager.getSubtask(subtaskId3));
        System.out.println("подзадача1 " + taskManager.getSubtask(subtaskId1));

        printHistory(taskManager.getHistory());
        taskManager.removeEpic(idEpic1);
        printHistory(taskManager.getHistory());
    }

    private static void testRemoveALL() {
        System.out.println("-------------------");
        System.out.println("Тест удаления");
        System.out.println("-------------------");
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        historyManager.clearHistory(); //отладочный метод
        taskManager.removeAllEpic();
        taskManager.removeAllTask();

        System.out.println("\nСоздание задач...");
        Task task1 = new Task(null, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(null, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        System.out.println("\nСоздание эпиков...");
        Epic epic1 = new Epic(null, "Эпик Спринт 1", "Завершить спринт 1", TaskStatus.NEW);
        Epic epic2 = new Epic(null, "Эпик Спринт 2", "Доделать спринт 2", TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListEpic();
        long idEpic1 = epics.get(0).getId();
        long idEpic2 = epics.get(1).getId();

        System.out.println("\nСоздание подзадач...");
        Subtask subtask1 = new Subtask(null, "Подзадача 1.1", "Выучить теорию 1", TaskStatus.NEW, idEpic1);
        Subtask subtask2 = new Subtask(null, "Подзадача 1.2", "просто подзадача 1.1", TaskStatus.NEW, idEpic1);
        Subtask subtask3 = new Subtask(null, "Подзадача 1.3", "Сдать проект 1.3", TaskStatus.NEW, idEpic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        printHistory(taskManager.getHistory());

        List<Task> tasks = taskManager.getListTask();
        System.out.println("\nСписок задач: " + tasks);
        long idTask1 = tasks.get(0).getId();
        long idTask2 = tasks.get(1).getId();
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));

        System.out.println("\nСписок эпиков: " + epics);
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));

        List<Subtask> subtasks = taskManager.getListSubtask();
        long subtaskId1 = subtasks.get(0).getId();
        long subtaskId2 = subtasks.get(1).getId();
        long subtaskId3 = subtasks.get(2).getId();
        System.out.println("\nСписок подзадач: " + subtasks);
        System.out.println("подзадача1 " + taskManager.getSubtask(subtaskId1));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtaskId2));
        System.out.println("подзадача3 " + taskManager.getSubtask(subtaskId3));
        printHistory(taskManager.getHistory());
        taskManager.removeAllTask();
        taskManager.removeAllSubtask();
        taskManager.removeAllEpic();
        taskManager.removeAllEpic();    //корректно работает с null
        printHistory(taskManager.getHistory());
    }

    private static void printHistory(List<Task> history) {
        int row = 0;
        System.out.println("-------------------");
        System.out.println("История просмотров задач:");
        for (Task task : history) {
            row++;
            System.out.println("row = " + row + " id=" + task.getId() + " name=" + task.getName());
        }
        System.out.println("-------------------");
    }
}