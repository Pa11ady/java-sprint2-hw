package ru.yandex.practicum.tasktracker.server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.enums.TaskStatus;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    protected final Long TASK_ID1 = 10L;
    protected final Long TASK_ID2 = 20L;
    protected final Long TASK_ID3 = 30L;

    protected final long EPIC_ID1 = 40L;
    protected final long EPIC_ID2 = 50L;
    protected final long EPIC_ID3 = 60L;

    protected final Long SUBTASK_ID1 = 100L;
    protected final Long SUBTASK_ID2 = 200L;
    protected final Long SUBTASK_ID3 = 3000L;

    protected final LocalDateTime taskDate2 = LocalDateTime.of(2020, 3, 3, 11, 0);
    protected final LocalDateTime taskDate3 = LocalDateTime.of(2020, 3, 3, 3, 0);

    protected final LocalDateTime subtaskDate2 = LocalDateTime.of(2021, 3, 3, 11, 0);
    protected final LocalDateTime subtaskDate3 = LocalDateTime.of(2021, 3, 3, 3, 0);

    protected final Integer taskDuration2 = 3 * 60;
    protected final Integer taskDuration3 = 50;

    protected final Integer subtaskDuration2 = 60 * 3;
    protected final Integer subtaskDuration3 = 50;

    protected final Integer epicDuration2 = 50 + 3 * 60;

    protected final Task task1 = new Task(TASK_ID1, "задача коробки", "Найти коробки", TaskStatus.NEW);
    protected final Task task2 = new Task(TASK_ID2, "задача вещи", "Собрать вещи", TaskStatus.DONE,
            taskDuration2, taskDate2);
    protected final Task task3 = new Task(TASK_ID3, "задача прочитать главу", "Прочитать главу книги",
            TaskStatus.IN_PROGRESS, taskDuration3, taskDate3);

    protected final Epic epic1 = new Epic(EPIC_ID1, "Эпик1", "Эпик 1 описание");
    protected final Epic epic2 = new Epic(EPIC_ID2, "Эпик2", "Эпик 2 описание", epicDuration2, subtaskDate3);
    protected final Epic epic3 = new Epic(EPIC_ID3, "Эпик3", "Эпик 3 описание");

    protected final Subtask subtask1 = new Subtask(SUBTASK_ID1, "Подзадача 1.1", "описание 1", TaskStatus.NEW, EPIC_ID1);
    protected final Subtask subtask2 = new Subtask(SUBTASK_ID2, "Подзадача 2.1", "описание 2.1",
            TaskStatus.NEW, EPIC_ID2, subtaskDuration2, subtaskDate2);
    protected final Subtask subtask3 = new Subtask(SUBTASK_ID3, "Подзадача 2.2", "просто 2.2", TaskStatus.NEW, EPIC_ID2,
            subtaskDuration3, subtaskDate3);

    protected void testAllFieldsTask(Task taskA, Task taskB) {
        String message = taskA.getClass().getSimpleName() + " не совпадают";
        assertEquals(taskA, taskB, message); //быстрая проверка
        assertAll(
                () -> assertEquals(taskA.getId(), taskB.getId(), message),
                () -> assertEquals(taskA.getName(), taskB.getName(), message),
                () -> assertEquals(taskA.getDescription(), taskB.getDescription(), message),
                () -> assertEquals(taskA.getStatus(), taskB.getStatus(), message),
                () -> assertEquals(taskA.getDuration(), taskB.getDuration(), message),
                () -> assertEquals(taskA.getStartTime(), taskB.getStartTime(), message)
        );

        if (taskA instanceof Epic && taskB instanceof Epic) {
            List<Long> listA = ((Epic) taskA).getListSubtaskId();
            List<Long> listB = ((Epic) taskB).getListSubtaskId();
            Collections.sort(listA);
            Collections.sort(listB);
            assertEquals(listA, listB);
        } else if (taskA instanceof Subtask && taskB instanceof Subtask) {
            assertEquals(((Subtask) taskA).getParentId(), ((Subtask) taskB).getParentId(), message);
        }
    }

    @BeforeAll
    static void startKVServer() throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }

    @Test
    void handleTest() {
    }

    @Test
    void handleTasksTask() {
    }

    @Test
    void handleTasksSubtask() {
    }

    @Test
    void handleTasksEpic() {
    }

    @Test
    void handleTasksSubtaskEpic() {
    }

    @Test
    void handleTasksHistory() {
    }

    @Test
    void handleTasks() {
    }
}