package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;
import ru.yandex.practicum.tasktracker.taskmanager.TaskManager;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("===============================");
        testWrongTask();
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
        testFinalSprint();  // Тест из Т. З.
    }
    private static void testTaskEpicSubtask() {
        Task task1 = new Task(TaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(TaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Epic epic1 = new Epic(TaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(TaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

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
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        TaskManager taskManager = new TaskManager();
        System.out.println("Создание Эпиков");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
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
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        TaskManager taskManager = new TaskManager();
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        System.out.println("Создание подзадач");
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        System.out.println(taskManager);
        System.out.println(taskManager.getListSubtaskFromEpic(epic1.getId()));

        System.out.println("Удаление подзадачи");
        taskManager.removeSubtask(subtask3.getId());
        System.out.println(taskManager);

        System.out.println("Удаление подзадач");
        taskManager.removeAllSubtask();
        System.out.println(taskManager);
        System.out.println(taskManager.getListEpic());
        System.out.println(taskManager.getListSubtask());
    }

    private static void testCreateUpdate() {
        System.out.println("----testCreateUpdate-----");
        Task task1 = new Task(TaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(TaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Epic epic1 = new Epic(TaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(TaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        TaskManager taskManager = new TaskManager();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        System.out.println("Добавили Задачи и Эпики");
        System.out.println(taskManager);
        System.out.println(taskManager.getListTask());

        taskManager.createSubtask(subtask1);
        System.out.println("Добавили подзадачу1");
        System.out.println(taskManager);
        System.out.println(taskManager.getSubtask(subtask1.getId()));

        taskManager.createSubtask(subtask2);
        System.out.println("Добавили подзадачу2");
        System.out.println(taskManager);

        taskManager.createSubtask(subtask3);
        System.out.println("Добавили подзадачу3");
        System.out.println(taskManager);

        task1.setStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);
        System.out.println("Обновили задачу1");
        System.out.println(taskManager.getTask(task1.getId()));
        //ничего не должно измениться
        task1.setStatus(TaskStatus.IN_PROGRESS);
        long lastId = task1.getId();
        task1.setId(100500);
        System.out.println("Не должно измениться");
        System.out.println(taskManager.getTask(lastId));

        taskManager.updateEpic(epic1);
        System.out.println("Обновили эпик1");
        System.out.println(taskManager);

        System.out.println("Обновили подзадачи");
        taskManager.updateSubtask(subtask1);
        taskManager.updateTask(subtask2);
        taskManager.updateTask(subtask3);
        System.out.println(taskManager);
        System.out.println(taskManager.getListSubtaskFromEpic(epic1.getId()));
        System.out.println(taskManager.getListSubtaskFromEpic(epic2.getId()));
        System.out.println(taskManager.getListSubtask());
    }

    private static void testGetList() {
        System.out.println("----testGetList-----");
        Task task1 = new Task(TaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(TaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Task task3 = new Task(TaskManager.calcNextTaskId(), "task3", "task3 done", TaskStatus.DONE);
        Task task4 = new Task(TaskManager.calcNextTaskId(), "task4", "task4 program", TaskStatus.IN_PROGRESS);

        Epic epic1 = new Epic(TaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(TaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(TaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        TaskManager taskManager = new TaskManager();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        System.out.println("Все задачи");
        System.out.println(taskManager.getListTask());
        System.out.println("Задачи 101 нет в списке " +taskManager.getTask(101));
        System.out.println("ID= " + task2.getId() + " " + taskManager.getTask(task2.getId()));
        System.out.println("Magic ID= " + 29 + " " + taskManager.getTask(29));
        System.out.println("Magic ID= " + 30 + " " + taskManager.getTask(30));

        System.out.println("Все эпики");
        System.out.println(taskManager.getListEpic());
        System.out.println("Эпик ID = " +epic2.getId() + " " + taskManager.getEpic(epic2.getId()));
        System.out.println("Все подзадачи");
        System.out.println(taskManager.getListSubtask());
        System.out.println("Все подзадачи " + epic2.getId() + " " + epic2.getName());
        System.out.println(taskManager.getListSubtaskFromEpic(epic2.getId()));
    }

    private static void testFinalSprint() {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ");
        System.out.println("-------------------");
        System.out.println("\nСоздание задач...");
        TaskManager taskManager = new TaskManager();
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

    private static void testWrongTask() {
        System.out.println("Проверяем устойчивость к ошибкам");
        System.out.println("-------------------------------");
        TaskManager taskManager = new TaskManager();
        taskManager.removeAllTask();
        System.out.println("Удали задачу, которой нет " + taskManager.removeTask(100500));
        System.out.println("Получили задачу, которой нет " + taskManager.getTask(100500));
        System.out.println("Пустой список задач " + taskManager.getListTask());
        System.out.println(taskManager);

        Task task1 = new Task(TaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        System.out.println("Обновили задачу = " + taskManager.updateTask(task1));
        System.out.println("Создали задачу = " + taskManager.createTask(task1));
        System.out.println("Создали задачу повторно = " + taskManager.createTask(task1));
        task1.setStatus(TaskStatus.DONE);
        System.out.println(taskManager.getListTask());
        System.out.println("Обновили задачу = " + taskManager.updateTask(task1));
        System.out.println(taskManager);
        System.out.println(taskManager.getTask(task1.getId()));
        System.out.println("Нет исключений");
    }
}