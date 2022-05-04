package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.enums.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {
    final Long EPIC_ID1 = 10L;
    final String MSG = "Неверный статус";

    TaskManager taskManager;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        epic1 = new Epic(EPIC_ID1, "Эпик Пустой", "Просто пустой эпик");
    }

    //Пустой список подзадач.
    @Test
    void testA_CreateEmptySubtaskList() {
        subtask1 = null;
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        assertEquals(TaskStatus.NEW, taskManager.getEpic(EPIC_ID1).getStatus(), MSG);
    }

    //Все подзадачи со статусом NEW.
    @Test
    void testB_CreateAllSubtasksWithStatusNew() {
        subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.NEW, EPIC_ID1);
        subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.NEW, EPIC_ID1);
        subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.NEW, EPIC_ID1);
        createTestData();
        assertEquals(TaskStatus.NEW, taskManager.getEpic(EPIC_ID1).getStatus(), MSG);
    }

    //Все подзадачи со статусом DONE.
    @Test
    void testC_CreateAllSubtasksWithStatusDone() {
        subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.DONE, EPIC_ID1);
        subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.DONE, EPIC_ID1);
        subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.DONE, EPIC_ID1);
        createTestData();
        assertEquals(TaskStatus.DONE, taskManager.getEpic(EPIC_ID1).getStatus(), MSG);
    }

    //Подзадачи со статусами NEW и DONE.
    @Test
    void testD_CreateAllSubtasksWithStatusNewAndDone() {
        subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.DONE, EPIC_ID1);
        subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.DONE, EPIC_ID1);
        subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.NEW, EPIC_ID1);
        createTestData();
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(EPIC_ID1).getStatus(), MSG);
    }

    //Подзадачи со статусом IN_PROGRESS.
    @Test
    void testE_CreateAllSubtasksWithStatusInProgress() {
        subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.IN_PROGRESS, EPIC_ID1);
        subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.IN_PROGRESS, EPIC_ID1);
        subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.IN_PROGRESS, EPIC_ID1);
        createTestData();
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(EPIC_ID1).getStatus(), MSG);
    }
    private void createTestData() {
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
    }
}