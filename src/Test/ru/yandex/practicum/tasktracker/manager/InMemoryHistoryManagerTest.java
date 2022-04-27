package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.task.Task;
import ru.yandex.practicum.tasktracker.task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    final Long TASK_ID1 = 10L;
    final Long TASK_ID2 = 20L;
    final Long TASK_ID3 = 30L;

    @Test
    void add() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        //Тест на пустые значения
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой");

        //Штатная работа
        Task task1 = new Task(TASK_ID1, "задача 1", "описание 1", TaskStatus.IN_PROGRESS);
        Task task2 = new Task(TASK_ID2, "задача 2", "описание 2", TaskStatus.IN_PROGRESS);
        historyManager.add(task1);
        historyManager.add(task2);
        List<Task> tasks = historyManager.getHistory();
        assertFalse(tasks.isEmpty(), "История не должна быть пустой");
        assertEquals(2, tasks.size(), "Неверное количество задач");
        assertEquals(TASK_ID1, tasks.get(0).getId(), "Задачи не совпадают");
        assertEquals(TASK_ID2, tasks.get(1).getId(), "Задачи не совпадают");

        //Тест на дубликаты
        historyManager.add(task2);
        historyManager.add(task1);
        historyManager.add(task1);
        tasks = historyManager.getHistory();
        assertEquals(2, tasks.size(), "Неверное количество задач");
        assertEquals(TASK_ID2, tasks.get(0).getId(), "Задачи не совпадают");
        assertEquals(TASK_ID1, tasks.get(1).getId(), "Задачи не совпадают");
    }

    @Test
    void getHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();

        //пустая история
        assertTrue(historyManager.getHistory().isEmpty(), "История должна быть пустой");

        //Штатная работа
        for(int i = 0; i < 5; i++) {
            Task task1 = new Task(TASK_ID1 + i, "задача" + i, "описание" + i, TaskStatus.IN_PROGRESS);
            historyManager.add(task1);
        }
        List<Task> tasks = historyManager.getHistory();
        assertEquals(5, tasks.size(), "Неверное количество задач");
        for(int i = 0; i < 5; i++) {
            assertEquals(TASK_ID1 + i, tasks.get(i).getId(), "Задачи не совпадают");
        }
    }

    @Test
    void remove() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(TASK_ID1, "задача 1", "описание 1", TaskStatus.IN_PROGRESS);
        Task task2 = new Task(TASK_ID2, "задача 2", "описание 2", TaskStatus.IN_PROGRESS);
        Task task3 = new Task(TASK_ID3, "задача 3", "описание 3", TaskStatus.IN_PROGRESS);
        List<Task> tasks;

        //Тест на пустые значения
        historyManager.remove(null);
        tasks = historyManager.getHistory();
        assertTrue(tasks.isEmpty(), "История должна быть пустой.");

        //удаление плюс дублирование
        historyManager.add(task1);
        historyManager.remove(TASK_ID1);
        historyManager.remove(TASK_ID1);
        tasks = historyManager.getHistory();
        assertTrue(tasks.isEmpty(), "История должна быть пустой.");

        //Удаление начало плюс дублирование
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(TASK_ID1);
        historyManager.remove(TASK_ID1);
        tasks = historyManager.getHistory();
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(TASK_ID2, tasks.get(0).getId(), "Задачи не совпадают");
        assertEquals(TASK_ID3, tasks.get(1).getId(), "Задачи не совпадают");

        //Удаление середина плюс дублирование
        historyManager.clearHistory();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(TASK_ID2);
        historyManager.remove(TASK_ID2);
        tasks = historyManager.getHistory();
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(TASK_ID1, tasks.get(0).getId(), "Задачи не совпадают");
        assertEquals(TASK_ID3, tasks.get(1).getId(), "Задачи не совпадают");

        //Удаление конец плюс дублирование
        historyManager.clearHistory();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(TASK_ID3);
        historyManager.remove(TASK_ID3);
        tasks = historyManager.getHistory();
        assertEquals(2, tasks.size(), "Неверное количество задач.");
        assertEquals(TASK_ID1, tasks.get(0).getId(), "Задачи не совпадают");
        assertEquals(TASK_ID2, tasks.get(1).getId(), "Задачи не совпадают");

        //окончательное удаление
        historyManager.remove(TASK_ID2);
        historyManager.remove(TASK_ID1);
        tasks = historyManager.getHistory();
        assertTrue(tasks.isEmpty(), "История должна быть пустой.");
    }

    @Test
    void clearHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        for(int i = 0; i < 5; i++) {
            Task task1 = new Task(TASK_ID1 + i, "задача" + i, "описание" + i, TaskStatus.IN_PROGRESS);
            historyManager.add(task1);
        }
        historyManager.clearHistory();
        List<Task> tasks = historyManager.getHistory();
        assertTrue(tasks.isEmpty(), "История должна быть пустой.");
    }
}