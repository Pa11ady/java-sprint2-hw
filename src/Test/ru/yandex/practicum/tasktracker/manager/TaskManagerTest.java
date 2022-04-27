package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract  class TaskManagerTest <T extends TaskManager> {
    private T taskManager;
    final Long TASK_ID1 = 10L;
    final Long TASK_ID2 = 20L;
    final Long TASK_ID3 = 30L;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    void setUp() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
    }

    @Test
    void getListTask() {

        assertTrue(true);
    }

    @Test
    void removeAllTask() {
    }

    @Test
    void getTask() {
        //Пустой список задач
        assertNull(taskManager.getTask(TASK_ID1));

        //Стандартное поведение
        Task task1 = new Task(TASK_ID1, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(TASK_ID2, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        assertTrue(taskManager.createTask(task1));
        assertTrue(taskManager.createTask(task2));
        Task task1Res = taskManager.getTask(TASK_ID1);
        assertAll(
                () -> assertEquals(task1.getId(), task1Res.getId()),
                () -> assertEquals(task1.getName(), task1Res.getName()),
                () -> assertEquals(task1.getDescription(), task1Res.getDescription()),
                () -> assertEquals(task1.getStatus(), task1Res.getStatus())
        );
        Task task2Res =  taskManager.getTask(TASK_ID2);
        assertAll(
                () -> assertEquals(task2.getId(), task2Res.getId()),
                () -> assertEquals(task2.getName(), task2Res.getName()),
                () -> assertEquals(task2.getDescription(), task2Res.getDescription()),
                () -> assertEquals(task2.getStatus(), task2Res.getStatus())
        );

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
        Task task1 = new Task(TASK_ID1, "задача коробки", "Найти коробки", TaskStatus.NEW);
        Task task2 = new Task(TASK_ID2, "задача вещи", "Собрать вещи", TaskStatus.NEW);
        assertTrue(taskManager.createTask(task1));
        assertTrue(taskManager.createTask(task2));
        tasks = taskManager.getListTask();
        assertEquals(2, tasks.size(), "Неверное количество задач");
        Task task1Res = taskManager.getTask(TASK_ID1);
        assertAll(
                () -> assertEquals(task1.getId(), task1Res.getId()),
                () -> assertEquals(task1.getName(), task1Res.getName()),
                () -> assertEquals(task1.getDescription(), task1Res.getDescription()),
                () -> assertEquals(task1.getStatus(), task1Res.getStatus())
        );
        Task task2Res =  taskManager.getTask(TASK_ID2);
        assertAll(
                () -> assertEquals(task2.getId(), task2Res.getId()),
                () -> assertEquals(task2.getName(), task2Res.getName()),
                () -> assertEquals(task2.getDescription(), task2Res.getDescription()),
                () -> assertEquals(task2.getStatus(), task2Res.getStatus())
        );

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
    }

    @Test
    void removeTask() {
    }

    @Test
    void getListEpic() {
    }

    @Test
    void removeAllEpic() {
    }

    @Test
    void getEpic() {
    }

    @Test
    void createEpic() {
    }

    @Test
    void updateEpic() {
    }

    @Test
    void removeEpic() {
    }

    @Test
    void getListSubtaskFromEpic() {
    }

    @Test
    void getListSubtask() {
    }

    @Test
    void removeAllSubtask() {
    }

    @Test
    void getSubtask() {
    }

    @Test
    void createSubtask() {
    }

    @Test
    void updateSubtask() {
    }

    @Test
    void removeSubtask() {
    }

    @Test
    void getHistory() {
    }
}