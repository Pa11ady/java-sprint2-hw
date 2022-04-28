package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

abstract  class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

    final Long TASK_ID1 = 10L;
    final Long TASK_ID2 = 20L;
    final Long TASK_ID3 = 30L;

    final long EPIC_ID1 = 40L;
    final long EPIC_ID2 = 50L;
    final long EPIC_ID3 = 60L;

    final Long SUBTASK_ID1 = 100L;
    final Long SUBTASK_ID2 = 200L;
    final Long SUBTASK_ID3 = 300L;


    final Task task1 = new Task(TASK_ID1, "задача коробки", "Найти коробки", TaskStatus.NEW);
    final Task task2 = new Task(TASK_ID2, "задача вещи", "Собрать вещи", TaskStatus.DONE);
    final Task task3 = new Task(TASK_ID3, "задача прочитать главу", "Прочитать главу книги", TaskStatus.IN_PROGRESS);

    final Epic epic1 = new Epic(EPIC_ID1, "Эпик1", "Эпик 1 описание");
    final Epic epic2 = new Epic(EPIC_ID2, "Эпик2", "Эпик 2 описание");
    final Epic epic3 = new Epic(EPIC_ID3, "Эпик2", "Эпик 3 описание");

    Subtask subtask1 = new Subtask(SUBTASK_ID1, "Подзадача 1.1", "описание 1", TaskStatus.NEW, EPIC_ID1);
    Subtask subtask2 = new Subtask(SUBTASK_ID2, "Подзадача 2.1", "описание 2.1", TaskStatus.NEW, EPIC_ID2);
    Subtask subtask3 = new Subtask(SUBTASK_ID3, "Подзадача 2.2", "просто 2.2", TaskStatus.NEW, EPIC_ID2);


    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;

        epic1.addSubtask(SUBTASK_ID1);
        epic2.addSubtask(SUBTASK_ID2);
        epic2.addSubtask(SUBTASK_ID3);

    }

    @BeforeEach
    void setUp() {

    }

    private void testAllFieldsTask(Task taskA, Task taskB) {
        String message = taskA.getClass().getSimpleName() + " не совпадают";
        assertEquals(taskA, taskB, message); //быстрая проверка
        assertAll(
                () -> assertEquals(taskA.getId(), taskB.getId(), message),
                () -> assertEquals(taskA.getName(), taskB.getName(), message),
                () -> assertEquals(taskA.getDescription(), taskB.getDescription(), message),
                () -> assertEquals(taskA.getStatus(), taskB.getStatus(), message)
        );

        if (taskA instanceof Epic && taskB instanceof Epic) {
            List<Long> listA = ((Epic) taskA).getListSubtaskId();
            List<Long> listB = ((Epic) taskB).getListSubtaskId();
            Collections.sort(listA);
            Collections.sort(listB);
            assertEquals(listA, listB);
        }
    }

    @Test
    void getListTask() {
        List<Task> tasks;

        //Пустой список задач
        tasks = taskManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустой");

        //Стандартное поведение
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        tasks = taskManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
        tasks.sort(Comparator.comparing(Task::getId));
        testAllFieldsTask(task1, tasks.get(0));
        testAllFieldsTask(task2, tasks.get(1));
        testAllFieldsTask(task3, tasks.get(2));
    }

    @Test
    void removeAllTask() {
        List<Task> tasks;

        //пустой список
        assertDoesNotThrow(taskManager::removeAllTask, "Не должно быть исключений");
        tasks = taskManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустой");

        //Стандартное поведение
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.removeAllTask();
        tasks = taskManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустой");
    }

    @Test
    void getTask() {
        //Пустой список задач
        assertNull(taskManager.getTask(TASK_ID1));

        //Стандартное поведение
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        testAllFieldsTask(task1, taskManager.getTask(TASK_ID1));
        testAllFieldsTask(task2, taskManager.getTask(TASK_ID2));

        //Неверные значения
        assertNull(taskManager.getTask(100500L));
        assertNull(taskManager.getTask(null));
    }

    @Test
    void createTask() {
        //Пустой список задач
        List<Task> tasks = taskManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустой");

        //Стандартное поведение
        assertTrue(taskManager.createTask(task1));
        assertTrue(taskManager.createTask(task2));
        tasks = taskManager.getListTask();
        assertEquals(2, tasks.size(), "Неверное количество задач");
        testAllFieldsTask(task1, taskManager.getTask(TASK_ID1));
        testAllFieldsTask(task2, taskManager.getTask(TASK_ID2));

        //Дубликат
        assertFalse(taskManager.createTask(task1));
        tasks = taskManager.getListTask();
        assertEquals(2, tasks.size(), "Неверное количество задач");

        //Неверное значение
        assertFalse(taskManager.createTask(null));
        tasks = taskManager.getListTask();
        assertEquals(2, tasks.size(), "Неверное количество задач");
    }

    @Test
    void updateTask() {
        Task task1_1 = new Task(TASK_ID1, "задача 3", "Описание 3", TaskStatus.DONE);
        List<Task> tasks;

        //Пустой список задач
        assertFalse(taskManager.updateTask(task1_1));
        tasks = taskManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустой");

        //Стандартное поведение
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        assertTrue(taskManager.updateTask(task1_1));
        tasks = taskManager.getListTask();
        assertEquals(2, tasks.size(), "Неверное количество задач");
        testAllFieldsTask(task1_1, taskManager.getTask(TASK_ID1));
        testAllFieldsTask(task2, taskManager.getTask(TASK_ID2));

        //Неверные значения
        assertFalse(taskManager.updateTask(null));
        assertFalse(taskManager.updateTask(task3));
    }

    @Test
    void removeTask() {
        List<Task> tasks;

        //Пустой список
        assertFalse(taskManager.removeTask(TASK_ID1));
        tasks = taskManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список задач должен быть пустой");

        //Стандартное поведение
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.removeTask(TASK_ID2);
        tasks = taskManager.getListTask();
        assertEquals(2, tasks.size(), "Неверное количество задач");
        assertNull(taskManager.getTask(TASK_ID2));
        assertEquals(task1, taskManager.getTask(TASK_ID1));
        assertEquals(task3,  taskManager.getTask(TASK_ID3));

        //Неверные значения
        assertFalse(taskManager.removeTask(null));
        assertFalse(taskManager.removeTask(TASK_ID2));
    }

    @Test
    void getListEpic() {
        List<Epic> epics;
        //Пустой список
        epics =taskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список Эпиков должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        epics = taskManager.getListEpic();
        assertEquals(3, epics.size(), "Неверное количество Эпиков");
        epics.sort(Comparator.comparing(Epic::getId));
        List<Epic> expectedEpics = List.of(epic1, epic2, epic3);
        assertEquals(expectedEpics, epics, "Эпики не совпадают");
    }

    @Test
    void removeAllEpic() {
        List<Epic> epics;
        //пустой список
        assertDoesNotThrow(taskManager::removeAllEpic, "Не должно быть исключений");
        epics = taskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список эпиков должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.removeAllEpic();
        epics = taskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список эпиков должен быть пустой");
    }

    @Test
    void getEpic() {
        //Пустой список задач
        assertNull(taskManager.getEpic(EPIC_ID1));

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        assertEquals(epic1, taskManager.getEpic(EPIC_ID1));
        assertEquals(epic2, taskManager.getEpic(EPIC_ID2));

        //Неверные значения
        assertNull(taskManager.getEpic(100500L));
        assertNull(taskManager.getEpic(null));
    }

    @Test
    void createEpic() {
        //Пустой список
        List<Epic> epics = taskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список эпиков должен быть пустой");

        //Стандартное поведение
        assertTrue(taskManager.createEpic(epic1));
        assertTrue(taskManager.createEpic(epic2));
        assertTrue(taskManager.createEpic(epic3));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        epics = taskManager.getListEpic();
        assertEquals(3, epics.size(), "Неверное количество эпиков");
        testAllFieldsTask(epic1, taskManager.getEpic(EPIC_ID1));
        testAllFieldsTask(epic2, taskManager.getEpic(EPIC_ID2));
        testAllFieldsTask(epic3, taskManager.getEpic(EPIC_ID3));

        //Дубликат
        assertFalse(taskManager.createEpic(epic1));
        epics = taskManager.getListEpic();
        assertEquals(3, epics.size(), "Неверное количество эпиков");

        //Неверное значение
        assertFalse(taskManager.createEpic(null));
        epics = taskManager.getListEpic();
        assertEquals(3, epics.size(), "Неверное количество эпиков");
    }

    @Test
    void updateEpic() {
        Epic epic1_1 = new Epic(EPIC_ID1, "Эпик 4", "Описание 4");
        List<Epic> epics;

        //Пустой список задач
        assertFalse(taskManager.updateEpic(epic1_1));
        epics = taskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        epic1_1.addSubtask(SUBTASK_ID1);
        taskManager.createEpic(epic3);
        assertTrue(taskManager.updateEpic(epic1_1));
        epics = taskManager.getListEpic();
        assertEquals(2, epics.size(), "Неверное количество");
        testAllFieldsTask(epic1_1, taskManager.getEpic(EPIC_ID1));
        testAllFieldsTask(epic3, taskManager.getEpic(EPIC_ID3));

        //Неверные значения
        assertFalse(taskManager.updateEpic(null));
        assertFalse(taskManager.updateEpic(epic2));
    }

    @Test
    void removeEpic() {
        List<Epic> epics;

        //Пустой список
        assertFalse(taskManager.removeEpic(EPIC_ID1));
        epics = taskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список задач должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.removeEpic(EPIC_ID2);
        epics = taskManager.getListEpic();
        assertEquals(2, epics.size(), "Неверное количество задач");
        assertNull(taskManager.getEpic(EPIC_ID2));
        assertEquals(epic1, taskManager.getEpic(EPIC_ID1));
        assertEquals(epic3,  taskManager.getEpic(EPIC_ID3));

        //Неверные значения
        assertFalse(taskManager.removeEpic(null));
        assertFalse(taskManager.removeEpic(EPIC_ID2));
    }

    @Test
    void getListSubtaskFromEpic() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void getListSubtask() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void removeAllSubtask() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void getSubtask() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void createSubtask() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void updateSubtask() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void removeSubtask() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void getHistory() {
        //Пустой список
        //Стандартное поведение
    }
}