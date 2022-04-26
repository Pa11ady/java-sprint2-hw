package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    final Long TASK_ID = 10L;

    @Test
    void add() {
        Task task1 = new Task(TASK_ID, "задача 1", "описание 1", TaskStatus.IN_PROGRESS);
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        List<Task> tasks = historyManager.getHistory();
        assertEquals(TASK_ID, tasks.get(0).getId());
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        for(int i = 0; i < 5; i++) {
            Task task1 = new Task(TASK_ID + i, "задача" + i, "описание" + i, TaskStatus.IN_PROGRESS);
            historyManager.add(task1);
        }
        List<Task> tasks = historyManager.getHistory();
        for(int i = 0; i < 5; i++) {
            assertEquals(TASK_ID + i, tasks.get(i).getId());
        }
    }

    @Test
    void remove() {
        Task task1 = new Task(TASK_ID, "задача 1", "описание 1", TaskStatus.IN_PROGRESS);
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        historyManager.remove(TASK_ID);
        List<Task> tasks = historyManager.getHistory();
        assertTrue(tasks.isEmpty());
    }

    @Test
    void clearHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        for(int i = 0; i < 5; i++) {
            Task task1 = new Task(TASK_ID + i, "задача" + i, "описание" + i, TaskStatus.IN_PROGRESS);
            historyManager.add(task1);
        }
        historyManager.clearHistory();
        List<Task> tasks = historyManager.getHistory();
        assertTrue(tasks.isEmpty());
    }

    //Пустая история задач.
    @Test
    void testA_EmptyHistory() {

    }

    //Дублирование.
    @Test
    void testB_Duplication() {

    }

    //Удаление из истории начало.
    @Test
    void testC1_DeleteFirst() {

    }

    //Удаление из истории середина.
    @Test
    void testC2_DeleteMiddle() {

    }

    //Удаление из истории конец.
    @Test
    void testC3_DeleteEnd() {

    }
}