package ru.yandex.practicum.tasktracker.mainapp;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;
import ru.yandex.practicum.tasktracker.manager.InMemoryTaskManager;

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
        Task task1 = new Task(InMemoryTaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(InMemoryTaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Epic epic1 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
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
        long id = InMemoryTaskManager.calcNextTaskId();
        Task task1 = new Task(id, "Задача " + id, "Важная задача " + id, TaskStatus.NEW);
        id = InMemoryTaskManager.calcNextTaskId();
        Task task2 = new Task(id, "Задача " + id, "Интересная задача " + id, TaskStatus.NEW);
        id = InMemoryTaskManager.calcNextTaskId();
        Task task3 = new Task(id, "Задача " + id, "Интересная задача " + id, TaskStatus.NEW);

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        System.out.println("Создание задач");
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        System.out.println(inMemoryTaskManager);

        System.out.println("Удаление задачи");
        inMemoryTaskManager.removeTask(task1.getId());
        System.out.println(inMemoryTaskManager);

        System.out.println("Удаление задачи");
        inMemoryTaskManager.removeAllTask();
        System.out.println(inMemoryTaskManager);
    }

    private static void testCreateDeleteEpic() {
        Epic epic1 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask2 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        System.out.println("Создание Эпиков");
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        System.out.println(inMemoryTaskManager);

        System.out.println("Удаление эпика");
        inMemoryTaskManager.removeEpic(epic2.getId());
        System.out.println(inMemoryTaskManager);

        System.out.println("Удаление эпиков");
        inMemoryTaskManager.removeAllEpic();
        System.out.println(inMemoryTaskManager);
    }

    private static void testCreateDeleteSubTask() {
        Epic epic1 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        System.out.println("Создание подзадач");
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.createSubtask(subtask3);
        System.out.println(inMemoryTaskManager);
        System.out.println(inMemoryTaskManager.getListSubtaskFromEpic(epic1.getId()));

        System.out.println("Удаление подзадачи");
        inMemoryTaskManager.removeSubtask(subtask3.getId());
        System.out.println(inMemoryTaskManager);

        System.out.println("Удаление подзадач");
        inMemoryTaskManager.removeAllSubtask();
        System.out.println(inMemoryTaskManager);
        System.out.println(inMemoryTaskManager.getListEpic());
        System.out.println(inMemoryTaskManager.getListSubtask());
    }

    private static void testCreateUpdate() {
        System.out.println("----testCreateUpdate-----");
        Task task1 = new Task(InMemoryTaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(InMemoryTaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Epic epic1 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        System.out.println("Добавили Задачи и Эпики");
        System.out.println(inMemoryTaskManager);
        System.out.println(inMemoryTaskManager.getListTask());

        inMemoryTaskManager.createSubtask(subtask1);
        System.out.println("Добавили подзадачу1");
        System.out.println(inMemoryTaskManager);
        System.out.println(inMemoryTaskManager.getSubtask(subtask1.getId()));

        inMemoryTaskManager.createSubtask(subtask2);
        System.out.println("Добавили подзадачу2");
        System.out.println(inMemoryTaskManager);

        inMemoryTaskManager.createSubtask(subtask3);
        System.out.println("Добавили подзадачу3");
        System.out.println(inMemoryTaskManager);

        task1.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.updateTask(task1);
        System.out.println("Обновили задачу1");
        System.out.println(inMemoryTaskManager.getTask(task1.getId()));
        //ничего не должно измениться
        task1.setStatus(TaskStatus.IN_PROGRESS);
        long lastId = task1.getId();
        task1.setId(100500);
        System.out.println("Не должно измениться");
        System.out.println(inMemoryTaskManager.getTask(lastId));

        inMemoryTaskManager.updateEpic(epic1);
        System.out.println("Обновили эпик1");
        System.out.println(inMemoryTaskManager);

        System.out.println("Обновили подзадачи");
        inMemoryTaskManager.updateSubtask(subtask1);
        inMemoryTaskManager.updateTask(subtask2);
        inMemoryTaskManager.updateTask(subtask3);
        System.out.println(inMemoryTaskManager);
        System.out.println(inMemoryTaskManager.getListSubtaskFromEpic(epic1.getId()));
        System.out.println(inMemoryTaskManager.getListSubtaskFromEpic(epic2.getId()));
        System.out.println(inMemoryTaskManager.getListSubtask());
    }

    private static void testGetList() {
        System.out.println("----testGetList-----");
        Task task1 = new Task(InMemoryTaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        Task task2 = new Task(InMemoryTaskManager.calcNextTaskId(), "Задача2", "Интересная задача 2", TaskStatus.NEW);
        Task task3 = new Task(InMemoryTaskManager.calcNextTaskId(), "task3", "task3 done", TaskStatus.DONE);
        Task task4 = new Task(InMemoryTaskManager.calcNextTaskId(), "task4", "task4 program", TaskStatus.IN_PROGRESS);

        Epic epic1 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 1", "Супер эпик 1.2", TaskStatus.NEW);
        Epic epic2 = new Epic(InMemoryTaskManager.calcNextTaskId(), "Эпик 2", "Классный эпик 2.1", TaskStatus.NEW);

        Subtask subtask1 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 1", "простая подзадача 1", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask1.getId());

        Subtask subtask2 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "просто подзадача 2", TaskStatus.NEW, epic1.getId());
        epic1.addSubtask(subtask2.getId());

        Subtask subtask3 = new Subtask(InMemoryTaskManager.calcNextTaskId(), "Подзадача 2", "легкая подзадача 2", TaskStatus.NEW, epic2.getId());
        epic2.addSubtask(subtask3.getId());

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createTask(task4);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.createSubtask(subtask3);

        System.out.println("Все задачи");
        System.out.println(inMemoryTaskManager.getListTask());
        System.out.println("Задачи 101 нет в списке " + inMemoryTaskManager.getTask(101));
        System.out.println("ID= " + task2.getId() + " " + inMemoryTaskManager.getTask(task2.getId()));
        System.out.println("Magic ID= " + 29 + " " + inMemoryTaskManager.getTask(29));
        System.out.println("Magic ID= " + 30 + " " + inMemoryTaskManager.getTask(30));

        System.out.println("Все эпики");
        System.out.println(inMemoryTaskManager.getListEpic());
        System.out.println("Эпик ID = " +epic2.getId() + " " + inMemoryTaskManager.getEpic(epic2.getId()));
        System.out.println("Все подзадачи");
        System.out.println(inMemoryTaskManager.getListSubtask());
        System.out.println("Все подзадачи " + epic2.getId() + " " + epic2.getName());
        System.out.println(inMemoryTaskManager.getListSubtaskFromEpic(epic2.getId()));
    }

    private static void testFinalSprint() {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ");
        System.out.println("-------------------");
        System.out.println("\nСоздание задач...");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task(0, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(0, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        List<Task> tasks = inMemoryTaskManager.getListTask();
        System.out.println("Список задач: " + tasks);
        long idTask1 = tasks.get(0).getId();
        long idTask2 = tasks.get(1).getId();
        System.out.println("Задача1 " + inMemoryTaskManager.getTask(idTask1));
        System.out.println("Задача2 " + inMemoryTaskManager.getTask(idTask2));

        System.out.println("\nСоздание эпиков...");
        Epic epic1 = new Epic(0, "Эпик Спринт 1", "Завершить спринт 1", TaskStatus.NEW);
        Epic epic2 = new Epic(0, "Эпик Спринт 2", "Доделать спринт 2", TaskStatus.NEW);
        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createEpic(epic2);
        List<Epic> epics = inMemoryTaskManager.getListEpic();
        long idEpic1 = epics.get(0).getId();
        long idEpic2 = epics.get(1).getId();
        System.out.println("Список эпиков: " + epics);
        System.out.println("Эпик 1 " + inMemoryTaskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + inMemoryTaskManager.getEpic(idEpic2));

        System.out.println("\nСоздание подзадач...");
        Subtask subtask1 = new Subtask(0, "Подзадача 1.1", "Выучить теорию 1", TaskStatus.NEW, idEpic1);
        Subtask subtask2 = new Subtask(0, "Подзадача 2.1", "просто подзадача 2.1", TaskStatus.NEW, idEpic2);
        Subtask subtask3 = new Subtask(0, "Подзадача 2.2", "Сдать проект 2.2", TaskStatus.NEW, idEpic2);
        inMemoryTaskManager.createSubtask(subtask1);
        inMemoryTaskManager.createSubtask(subtask2);
        inMemoryTaskManager.createSubtask(subtask3);
        List<Subtask> subtasks = inMemoryTaskManager.getListSubtask();
        long subtaskId1 = subtasks.get(0).getId();
        long subtaskId2 = subtasks.get(1).getId();
        long subtaskId3 = subtasks.get(2).getId();
        System.out.println("Список подзадач: " + subtasks);
        System.out.println("подзадача1 " + inMemoryTaskManager.getSubtask(subtaskId1));
        System.out.println("подзадача2 " + inMemoryTaskManager.getSubtask(subtaskId2));
        System.out.println("подзадача3 " + inMemoryTaskManager.getSubtask(subtaskId3));
        System.out.println("Эпики: ");
        System.out.println("Эпик 1 " + inMemoryTaskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + inMemoryTaskManager.getEpic(idEpic2));

        System.out.println("\nИзменение задач");
        task1 = new Task(tasks.get(0));
        task2 = new Task(tasks.get(1));
        task1.setStatus(TaskStatus.DONE);
        task2.setStatus(TaskStatus.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(task2);
        System.out.println("Задача1 " + inMemoryTaskManager.getTask(idTask1));
        System.out.println("Задача2 " + inMemoryTaskManager.getTask(idTask2));

        System.out.println("\nИзменение подзадач");
        subtask1 = new Subtask(subtasks.get(0));
        subtask2 = new Subtask(subtasks.get(1));
        subtask3 = new Subtask(subtasks.get(2));
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.NEW);
        inMemoryTaskManager.updateSubtask(subtask2);
        inMemoryTaskManager.updateSubtask(subtask3);
        System.out.println("\nУдалить задачу");
        inMemoryTaskManager.removeTask(idTask1);
        System.out.println("Задача1 " + inMemoryTaskManager.getTask(idTask1));
        System.out.println("Задача2 " + inMemoryTaskManager.getTask(idTask2));

        System.out.println("\nУдалить эпик");
        inMemoryTaskManager.removeEpic(idEpic1);
        System.out.println("Эпик 1 " + inMemoryTaskManager.getEpic(idEpic1));
        System.out.println("Эпик 2 " + inMemoryTaskManager.getEpic(idEpic2));
        System.out.println("Список подзадач: " + subtasks);
        System.out.println("подзадача1 " + inMemoryTaskManager.getSubtask(subtask1.getId()));
        System.out.println("подзадача2 " + inMemoryTaskManager.getSubtask(subtask2.getId()));
        System.out.println("подзадача3 " + inMemoryTaskManager.getSubtask(subtask3.getId()));
    }

    private static void testWrongTask() {
        System.out.println("Проверяем устойчивость к ошибкам");
        System.out.println("-------------------------------");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        inMemoryTaskManager.removeAllTask();
        System.out.println("Удали задачу, которой нет " + inMemoryTaskManager.removeTask(100500));
        System.out.println("Получили задачу, которой нет " + inMemoryTaskManager.getTask(100500));
        System.out.println("Пустой список задач " + inMemoryTaskManager.getListTask());
        System.out.println(inMemoryTaskManager);

        Task task1 = new Task(InMemoryTaskManager.calcNextTaskId(), "Задача1", "Важная задача 1", TaskStatus.NEW);
        System.out.println("Обновили задачу = " + inMemoryTaskManager.updateTask(task1));
        System.out.println("Создали задачу = " + inMemoryTaskManager.createTask(task1));
        System.out.println("Создали задачу повторно = " + inMemoryTaskManager.createTask(task1));
        task1.setStatus(TaskStatus.DONE);
        System.out.println(inMemoryTaskManager.getListTask());
        System.out.println("Обновили задачу = " + inMemoryTaskManager.updateTask(task1));
        System.out.println(inMemoryTaskManager);
        System.out.println(inMemoryTaskManager.getTask(task1.getId()));
        System.out.println("Нет исключений");
    }
}