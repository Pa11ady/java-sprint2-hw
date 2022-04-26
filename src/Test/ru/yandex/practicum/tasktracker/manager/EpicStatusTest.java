package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {
    final Long EPIC_ID1 = 10L;

    //Пустой список подзадач.
    @Test
    void testA_CreateEmptySubtaskList() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(EPIC_ID1, "Эпик Пустой", "Просто пустой эпик");
        Subtask subtask1 = null;
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        assertEquals(TaskStatus.NEW, taskManager.getEpic(EPIC_ID1).getStatus());
    }

    //Все подзадачи со статусом NEW.
    @Test
    void testB_CreateAllSubtasksWithStatusNew() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(EPIC_ID1, "Эпик1", "Эпик 1 описание");
        Subtask subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.NEW, EPIC_ID1);
        Subtask subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.NEW, EPIC_ID1);
        Subtask subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.NEW, EPIC_ID1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        assertEquals(TaskStatus.NEW, taskManager.getEpic(EPIC_ID1).getStatus());
    }

    //Все подзадачи со статусом DONE.
    @Test
    void testC_CreateAllSubtasksWithStatusDone() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(EPIC_ID1, "Эпик1", "Эпик 1 описание");
        Subtask subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.DONE, EPIC_ID1);
        Subtask subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.DONE, EPIC_ID1);
        Subtask subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.DONE, EPIC_ID1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        assertEquals(TaskStatus.DONE, taskManager.getEpic(EPIC_ID1).getStatus());
    }

    //Подзадачи со статусами NEW и DONE.
    @Test
    void testD_CreateAllSubtasksWithStatusNewAndDone() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(EPIC_ID1, "Эпик1", "Эпик 1 описание");
        Subtask subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.DONE, EPIC_ID1);
        Subtask subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.DONE, EPIC_ID1);
        Subtask subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.NEW, EPIC_ID1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(EPIC_ID1).getStatus());
    }

    //Подзадачи со статусом IN_PROGRESS.
    @Test
    void testE_CreateAllSubtasksWithStatusInProgress() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic(EPIC_ID1, "Эпик1", "Эпик 1 описание");
        Subtask subtask1 = new Subtask(null, "Подзадача 1", "111", TaskStatus.IN_PROGRESS, EPIC_ID1);
        Subtask subtask2 = new Subtask(null, "Подзадача 2", "222", TaskStatus.IN_PROGRESS, EPIC_ID1);
        Subtask subtask3 = new Subtask(null, "Подзадача 3", "333", TaskStatus.IN_PROGRESS, EPIC_ID1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        assertEquals(TaskStatus.IN_PROGRESS, taskManager.getEpic(EPIC_ID1).getStatus());
    }
}