package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;
import ru.yandex.practicum.tasktracker.taskmanager.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("====Проверка Создания задач====");
        testTaskEpicSubtask();
        System.out.println("===============================");
        System.out.println("====Проверка работы менеджера====");
        testCreateDeleteTask();
        testCreateDeleteEpic();
        testCreateDeleteSubTask();
        testCreateUpdate();
        testGetList();
        System.out.println("-------------------------------");
        testFinalSprint();
    }
    private static void testTaskEpicSubtask() {
        Task task1 = new Task(TaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(TaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Epic epic1 = new Epic(TaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(TaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubTask(subtask1.getId());

        Subtask subtask2 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubTask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubTask(subtask3.getId());

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println("------");
        System.out.println("Subtask " + epic1.getListSubtaskId());
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

    private static void testCreateDeleteEpic() {
        Epic epic1 = new Epic(TaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(TaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask2 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubTask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubTask(subtask3.getId());

        TaskManager taskManager = new TaskManager();
        System.out.println("Создание Эпиков");
        taskManager.createTask(epic1);
        taskManager.createTask(epic1);
        System.out.println(taskManager);

        System.out.println("Удаление эпика");
        taskManager.removeEpic(epic2.getId());
        System.out.println(taskManager);

        System.out.println("Удаление эпиков");
        taskManager.removeAllEpic();
        System.out.println(taskManager);
    }

    private static void testCreateDeleteSubTask() {
        Epic epic1 = new Epic(TaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(TaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubTask(subtask1.getId());

        Subtask subtask2 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubTask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubTask(subtask3.getId());

        TaskManager taskManager = new TaskManager();
        System.out.println("Создание подзадач");
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        System.out.println(taskManager);

        System.out.println("Удаление подзадачи");
        taskManager.removeSubtask(subtask3.getId());
        System.out.println(taskManager);

        System.out.println("Удаление подзадач");
        taskManager.removeAllSubtask();
        System.out.println(taskManager);
    }

    private static void testCreateUpdate() {
        System.out.println("----testCreateUpdate-----");

    }

    private static void testGetList() {
        System.out.println("----testGetList-----");
    }

    private static void testFinalSprint() {
        System.out.println("Финальный тест из ТЗ");

    }
}