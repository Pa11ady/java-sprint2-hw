package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.exception.ManagerTaskValidationException;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

abstract public class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

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

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;

        epic1.addSubtask(SUBTASK_ID1);
        epic2.addSubtask(SUBTASK_ID2);
        epic2.addSubtask(SUBTASK_ID3);
    }

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
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks(); //todo
        //assertTrue(tasks.isEmpty(), "Список задач должен быть пустой");
        //assertTrue(prioritizedTasks.isEmpty(), "Список упорядоченных задач должен быть пустой");
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
        assertTrue(taskManager.createTask(task3));
        tasks = taskManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
        testAllFieldsTask(task1, taskManager.getTask(TASK_ID1));
        testAllFieldsTask(task2, taskManager.getTask(TASK_ID2));
        testAllFieldsTask(task3, taskManager.getTask(TASK_ID3));

        LocalDateTime endDate2 = LocalDateTime.of(2020, 3, 3, 14, 0);
        LocalDateTime endDate3 = LocalDateTime.of(2020, 3, 3, 3, 50);
        assertNull(task1.getEndTime());
        assertEquals(endDate2, task2.getEndTime());
        assertEquals(endDate3, task3.getEndTime());

        //Дубликат
        assertFalse(taskManager.createTask(task1));
        tasks = taskManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");

        //Неверное значение
        assertFalse(taskManager.createTask(null));
        tasks = taskManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
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
        assertTrue(taskManager.removeTask(TASK_ID2));
        tasks = taskManager.getListTask();
        assertEquals(2, tasks.size(), "Неверное количество задач");
        assertNull(taskManager.getTask(TASK_ID2));
        assertEquals(task1, taskManager.getTask(TASK_ID1));
        assertEquals(task3,  taskManager.getTask(TASK_ID3));
        assertTrue(taskManager.removeTask(TASK_ID1));
        assertTrue(taskManager.removeTask(TASK_ID3));
        //List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        //assertTrue(prioritizedTasks.isEmpty(), "Список упорядоченных задач должен быть пустой");

        //Неверные значения
        assertFalse(taskManager.removeTask(null));
        assertFalse(taskManager.removeTask(TASK_ID2));
    }

    @Test
    void getListEpic() {
        List<Epic> epics;
        //Пустой список
        epics = taskManager.getListEpic();
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
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.removeAllEpic();
        epics = taskManager.getListEpic();
        List<Subtask> subtasks = taskManager.getListSubtask();
        assertTrue(epics.isEmpty(), "Список эпиков должен быть пустой");
        assertTrue(subtasks.isEmpty(), "Список упорядоченных задач должен быть пустой");
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertTrue(prioritizedTasks.isEmpty(), "Список упорядоченных задач должен быть пустой");
    }

    @Test
    void getEpic() {
        //Пустой список
        assertNull(taskManager.getEpic(EPIC_ID1));

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1);
        assertEquals(epic1, taskManager.getEpic(EPIC_ID1));
        assertEquals(epic3, taskManager.getEpic(EPIC_ID3));

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

        List<Subtask> subtasks =  taskManager.getListSubtaskFromEpic(EPIC_ID1);
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0));

        subtasks =  taskManager.getListSubtaskFromEpic(EPIC_ID2);
        assertEquals(2, subtasks.size(), "Неверное количество подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        List<Subtask> expectedSubtask = List.of(subtask2, subtask3);
        assertEquals(expectedSubtask, subtasks, "Подзадачи не совпадают");

        subtasks = taskManager.getListSubtaskFromEpic(EPIC_ID3);
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пустой");
        LocalDateTime endTime1 = taskManager.getEpic(EPIC_ID1).getEndTime();
        assertNull(endTime1);

        LocalDateTime endTime2 = taskManager.getEpic(EPIC_ID2).getEndTime();
        assertEquals(subtask2.getEndTime(),  endTime2);

        LocalDateTime endTime3 = taskManager.getEpic(EPIC_ID3).getEndTime();
        assertNull(endTime3);

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

        //Пустой список
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
        taskManager.createSubtask(subtask1);
        assertTrue(taskManager.removeEpic(EPIC_ID2));
        epics = taskManager.getListEpic();
        assertEquals(2, epics.size(), "Неверное количество задач");
        assertNull(taskManager.getEpic(EPIC_ID2));
        assertEquals(epic1, taskManager.getEpic(EPIC_ID1));
        assertEquals(epic3,  taskManager.getEpic(EPIC_ID3));
        assertTrue(taskManager.removeEpic(EPIC_ID1));
        assertTrue(taskManager.removeEpic(EPIC_ID3));
        epics = taskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список  должен быть пустой");
        List<Subtask> subtasks = taskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список задач должен быть пустой");


        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertTrue(prioritizedTasks.isEmpty(), "Список упорядоченных задач должен быть пустой");

        //Неверные значения
        assertFalse(taskManager.removeEpic(null));
        assertFalse(taskManager.removeEpic(EPIC_ID2));
    }

    @Test
    void getListSubtaskFromEpic() {
        List<Subtask> subtasks;

        //Пустой список
        taskManager.createEpic(epic3);
        subtasks = taskManager.getListSubtaskFromEpic(EPIC_ID3);
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        subtasks = taskManager.getListSubtaskFromEpic(EPIC_ID2);
        assertEquals(2, subtasks.size(), "Неверное количество подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        List<Subtask> expectedSubtask = List.of(subtask2, subtask3);
        assertEquals(expectedSubtask, subtasks, "Подзадачи не совпадают");

        //Неверные значения
        subtasks = taskManager.getListSubtaskFromEpic(100500L);
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пустой");
        subtasks = taskManager.getListSubtaskFromEpic(null);
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пустой");

    }

    @Test
    void getListSubtask() {
        List<Subtask> subtasks;
        //Пустой список
        subtasks = taskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");
        taskManager.createEpic(epic3);
        subtasks = taskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        subtasks = taskManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        List<Subtask> expectedSubtasks = List.of(subtask1, subtask2, subtask3);
        assertEquals(expectedSubtasks, subtasks, "Подзадач	не совпадают");
    }

    @Test
    void removeAllSubtask() {
        List<Subtask> subtask;
        //пустой список
        assertDoesNotThrow(taskManager::removeAllSubtask, "Не должно быть исключений");
        subtask = taskManager.getListSubtask();
        assertTrue(subtask.isEmpty(), "Список Подзадач должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.removeAllSubtask();
        subtask = taskManager.getListSubtask();
        assertTrue(subtask.isEmpty(), "Список Подзадач должен быть пустой");
    }

    @Test
    void getSubtask() {
        //Пустой список
        assertNull(taskManager.getSubtask(SUBTASK_ID1));

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(subtask1, taskManager.getSubtask(SUBTASK_ID1));
        assertEquals(subtask2, taskManager.getSubtask(SUBTASK_ID2));
        assertEquals(subtask3, taskManager.getSubtask(SUBTASK_ID3));

        //Неверные значения
        assertNull(taskManager.getSubtask(100500L));
        assertNull(taskManager.getSubtask(null));
    }

    @Test
    void createSubtask() {
        List<Subtask> subtasks;

        //Пустой список
        subtasks = taskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        assertTrue(taskManager.createSubtask(subtask1));
        assertTrue(taskManager.createSubtask(subtask2));
        assertTrue(taskManager.createSubtask(subtask3));

        subtasks = taskManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");
        testAllFieldsTask(subtask1, taskManager.getSubtask(SUBTASK_ID1));
        testAllFieldsTask(subtask2, taskManager.getSubtask(SUBTASK_ID2));
        testAllFieldsTask(subtask3, taskManager.getSubtask(SUBTASK_ID3));

        subtasks =  taskManager.getListSubtaskFromEpic(EPIC_ID1);
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают");
        subtasks =  taskManager.getListSubtaskFromEpic(EPIC_ID2);
        assertEquals(2, subtasks.size(), "Неверное количество подзадач");
        List<Subtask> expectedSubtask = List.of(subtask2, subtask3);
        assertEquals(expectedSubtask, subtasks, "Подзадачи не совпадают");
        subtasks = taskManager.getListSubtaskFromEpic(EPIC_ID3);
        assertTrue(subtasks.isEmpty(), "Список подзадач должен быть пустой");

        //Дубликат
        assertFalse(taskManager.createSubtask(subtask1));
        subtasks = taskManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");

        //Неверное значение
        assertFalse(taskManager.createSubtask(null));
        subtasks = taskManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");
    }

    @Test
    void updateSubtask() {
        Subtask subtask1_1 = new Subtask(SUBTASK_ID1, "Подзадача 100", "описание 100", TaskStatus.DONE, EPIC_ID1);
        List<Subtask> subtasks;

        //Пустой список
        assertFalse(taskManager.updateSubtask(subtask1_1));
        subtasks = taskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        assertTrue(taskManager.updateSubtask(subtask1_1));
        subtasks = taskManager.getListSubtask();
        assertEquals(1, subtasks.size(), "Неверное количество");
        testAllFieldsTask(subtask1_1, taskManager.getSubtask(SUBTASK_ID1));
        assertEquals(TaskStatus.DONE, taskManager.getEpic(EPIC_ID1).getStatus());

        //Неверные значения
        assertFalse(taskManager.updateSubtask(null));
        assertFalse(taskManager.updateSubtask(subtask3));
    }

    @Test
    void removeSubtask() {
        List<Subtask> subtasks;

        //Пустой список
        assertFalse(taskManager.removeSubtask(SUBTASK_ID1));
        subtasks = taskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список  должен быть пустой");

        //Стандартное поведение
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        assertTrue(taskManager.removeSubtask(SUBTASK_ID1));
        subtasks = taskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список  должен быть пустой");
        List<Long> idSubtasks = taskManager.getEpic(EPIC_ID1).getListSubtaskId();
        assertTrue(idSubtasks.isEmpty(), "Список  должен быть пустой");

        taskManager.removeAllEpic();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertTrue(prioritizedTasks.isEmpty(), "Список упорядоченных задач должен быть пустой");

        //Неверные значения
        assertFalse(taskManager.removeSubtask(null));
        assertFalse(taskManager.removeSubtask(SUBTASK_ID1));
    }

    @Test
    void getHistory() {
        List<Task> tasks;

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        //Пустой список
        tasks = taskManager.getHistory();
        assertTrue(tasks.isEmpty(), "Список  должен быть пустой");

        //Стандартное поведение
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        taskManager.getSubtask(subtask3.getId());
        tasks = taskManager.getHistory();
        List<Task> expectedTasks = List.of( task1, task2, epic1, epic2, subtask1, subtask2, subtask3);
        assertEquals(expectedTasks, tasks, "История не совпадает");

        //дубликаты
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic2.getId());
        taskManager.getSubtask(subtask3.getId());
        taskManager.getSubtask(subtask3.getId());
        taskManager.getSubtask(subtask3.getId());
        tasks = taskManager.getHistory();
        expectedTasks = List.of(task2, epic1,  subtask1, subtask2, task1, epic2, subtask3);
        assertEquals(expectedTasks, tasks, "История не совпадает");

        //удаление
        taskManager.removeTask(task2.getId());
        taskManager.removeEpic(epic1.getId());
        taskManager.removeSubtask(subtask3.getId());
        tasks = taskManager.getHistory();
        expectedTasks = List.of(subtask2, task1, epic2);
        assertEquals(expectedTasks, tasks, "История не совпадает");

        //Неверные значения
        taskManager.getTask(null);
        taskManager.getEpic(null);
        taskManager.getSubtask(null);
        taskManager.getTask(task2.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask3.getId());
        tasks = taskManager.getHistory();
        assertEquals(expectedTasks, tasks, "История не совпадает");
    }

    @Test
    void testGenerateID() {
        Long startID = InMemoryTaskManager.calcNextTaskId();
        final String MSG = "ID не совпадают";
        final Task task01 = new Task("44", "44", TaskStatus.NEW);
        final Task task02 = new Task("55", "55", TaskStatus.NEW);
        taskManager.createTask(task01);
        taskManager.createTask(task02);
        List<Task> tasks = taskManager.getListTask();
        tasks.sort(Comparator.comparing(Task::getId));
        assertEquals(startID + 1, tasks.get(0).getId(), MSG);
        assertEquals(startID + 2, tasks.get(1).getId(), MSG);

        final Epic epic03 = new Epic("66", "66");
        final Epic epic04 = new Epic("77", "77");
        taskManager.createEpic(epic03);
        taskManager.createEpic(epic04);
        List<Epic> epics = taskManager.getListEpic();
        epics.sort(Comparator.comparing(Epic::getId));
        Long epicID_03 = epics.get(0).getId();
        Long epicID_04 = epics.get(1).getId();
        assertEquals(startID + 3, epicID_03, MSG);
        assertEquals(startID + 4, epicID_04, MSG);

        final Subtask subtask05 = new Subtask("88", "88", TaskStatus.NEW, epicID_03);
        final Subtask subtask06 = new Subtask( "99", "99", TaskStatus.NEW, epicID_04);
        taskManager.createSubtask(subtask05);
        taskManager.createSubtask(subtask06);
        List<Subtask> subtasks = taskManager.getListSubtask();
        subtasks.sort(Comparator.comparing(Subtask::getId));
        assertEquals(startID + 5, subtasks.get(0).getId(), MSG);
        assertEquals(startID + 6, subtasks.get(1).getId(), MSG);
    }

    @Test
    void getPrioritizedTasks() {
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(6, prioritizedTasks.size());
        List<Long> expectedKeys = List.of(30L, 20L, 3000L, 200L, 10L, 100L);
        List<Long> actualKeys = new ArrayList<>();
        for (Task task : prioritizedTasks) {
            actualKeys.add(task.getId());
        }
        assertEquals(expectedKeys, actualKeys, "Ключи не совпадают!");

        taskManager.removeTask(30L);
        prioritizedTasks = taskManager.getPrioritizedTasks();
        actualKeys.clear();
        for (Task task : prioritizedTasks) {
            actualKeys.add(task.getId());
        }
        expectedKeys = List.of(20L, 3000L, 200L, 10L, 100L);
        assertEquals(expectedKeys, actualKeys, "Ключи не совпадают!");
    }

    @Test
    void testValidationTask() {
        //Пропускаем для HttpClientTestTaskManager, так серверное исключение не дойдёт до клиента
        assumeFalse("HttpClientTestTaskManager".equals(taskManager.getClass().getSimpleName()));

        long  ID = 100;
        final LocalDateTime taskDate1 = LocalDateTime.of(2021, 1, 1, 10, 0);
        final LocalDateTime taskDate2 = LocalDateTime.of(2021, 1, 1, 15, 0);
        final LocalDateTime taskDate3 = LocalDateTime.of(2021, 1, 1, 20, 0);

        final Task task1 = new Task("t1", "t1", TaskStatus.NEW, 120, taskDate1);  //10.00-12.00
        final Task task2 = new Task("t2", "t2", TaskStatus.NEW, 180, taskDate2);  //15.00-18.00
        final Task task3 = new Task("t3", "t3", TaskStatus.NEW, 60, taskDate3);   //20.00-21.00
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

        //полностью совпадает с t1
        Task testTask = new Task("t", "t", TaskStatus.NEW, 120, taskDate1);
        assertThrows(ManagerTaskValidationException.class, () -> taskManager.createTask(testTask));

        //внутри т2
        LocalDateTime testDate = LocalDateTime.of(2021, 1, 1, 16, 0);
        Task testTask1 = new Task("t", "t", TaskStatus.NEW, 60, testDate);
        assertThrows(ManagerTaskValidationException.class, () -> taskManager.createTask(testTask1));

        //внутри т3
        testDate =  LocalDateTime.of(2021, 1, 1, 20, 0);
        Task testTask3 = new Task("t", "t", TaskStatus.NEW, 30, testDate);
        assertThrows(ManagerTaskValidationException.class, () -> taskManager.createTask(testTask3));

        //перекрывает несколько зон
        testDate =  LocalDateTime.of(2021, 1, 1, 13, 0);
        Task testTask3_ = new Task("t", "t", TaskStatus.NEW, 6 * 60, testDate);
        assertThrows(ManagerTaskValidationException.class, () -> taskManager.createTask(testTask3_));

        //без исключений
        testDate = LocalDateTime.of(2021, 1, 1, 9, 0);
        Task testTask4 = new Task(ID, "t", "t", TaskStatus.NEW, 60, testDate);
        assertDoesNotThrow(() -> taskManager.createTask(testTask4));

        taskManager.removeTask(ID);
        testDate = LocalDateTime.of(2021, 1, 1, 13, 0);
        Task testTask5 = new Task(ID, "t", "t", TaskStatus.NEW, 60, testDate);
        assertDoesNotThrow(() -> taskManager.createTask(testTask5));

        taskManager.removeTask(ID);
        testDate =  LocalDateTime.of(2021, 1, 1, 18, 0);
        Task testTask6 = new Task(ID, "t", "t", TaskStatus.NEW, 120, testDate);
        assertDoesNotThrow(() -> taskManager.createTask(testTask6));

        taskManager.removeTask(ID);
        testDate =  LocalDateTime.of(2021, 1, 1, 21, 0);
        Task testTask7 = new Task(ID, "t", "t", TaskStatus.NEW, 60, testDate);
        assertDoesNotThrow(() -> taskManager.createTask(testTask7));
    }

    protected void createSampleStandard() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        //заполняем историю
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getEpic(epic3.getId());
        taskManager.getSubtask(subtask1.getId());
    }

    protected void createSampleWithoutHistory() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
    }

}