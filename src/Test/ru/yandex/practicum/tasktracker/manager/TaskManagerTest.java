package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract  class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

    final Long TASK_ID1 = 10L;
    final Long TASK_ID2 = 20L;
    final Long TASK_ID3 = 30L;

    final Task task1 = new Task(TASK_ID1, "задача коробки", "Найти коробки", TaskStatus.NEW);
    final Task task2 = new Task(TASK_ID2, "задача вещи", "Собрать вещи", TaskStatus.NEW);
    final Task task3 = new Task(TASK_ID3, "задача прочитать главу", "Прочитать главу книги", TaskStatus.NEW);

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    void setUp() {

    }

    private void testAllFieldsTask(Task taskA, Task taskB) {
        String message = "Задачи не совпадают";
        assertAll(
                () -> assertEquals(taskA.getId(), taskB.getId(), message),
                () -> assertEquals(taskA.getName(), taskB.getName(), message),
                () -> assertEquals(taskA.getDescription(), taskB.getDescription(), message),
                () -> assertEquals(taskA.getStatus(), taskB.getStatus(), message)
        );
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

        //List<Task> tasks
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
    }

    @Test
    void removeAllEpic() {
        //Пустой список задач
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void getEpic() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void createEpic() {
        //Пустой список задач
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void updateEpic() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
    }

    @Test
    void removeEpic() {
        //Пустой список
        //Стандартное поведение
        //Неверные значения
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