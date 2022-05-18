package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.server.KVServer;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    static final int PORT = 8070;
    final static String KV_URL = "http://localhost:" + PORT;
    public HttpTaskManagerTest() {
        super(new HttpTaskManager(KV_URL));
    }

   @BeforeAll
   static void startKVServer() throws IOException {
       new KVServer(PORT).start();
   }

    @BeforeEach
    void setUp() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
    }

    @Test
    void loadFromUrl() {
        HttpTaskManager readHttpTaskManager;
        List<Task> tasks;
        List<Epic> epics;
        List<Subtask> subtasks;

        //Пустой список задач
        readHttpTaskManager =  HttpTaskManager.loadFromUrl(KV_URL);
        tasks = readHttpTaskManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список Задач должен быть пустой");

        epics = readHttpTaskManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список Эпиков должен быть пустой");

        subtasks = readHttpTaskManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        tasks = readHttpTaskManager.getHistory();
        assertTrue(tasks.isEmpty(), "Список  должен быть пустой");

        //Стандартное поведение
        createSampleStandard();
        readHttpTaskManager = HttpTaskManager.loadFromUrl(KV_URL);
        testSampleStandard(readHttpTaskManager);

        taskManager.removeAllTask();
        taskManager.removeAllEpic();

        //Эпик без подзадач
        taskManager.createEpic(epic3);
        taskManager.getEpic(epic3.getId());
        readHttpTaskManager = HttpTaskManager.loadFromUrl(KV_URL);
        testEpicWithoutSubtasks(readHttpTaskManager);

        taskManager.removeAllTask();
        taskManager.removeAllEpic();

        //Без истории
        createSampleWithoutHistory();
        readHttpTaskManager = HttpTaskManager.loadFromUrl(KV_URL);
        testSampleWithoutHistory(readHttpTaskManager);
    }

    private void testSampleStandard(HttpTaskManager readHttpTaskManager) {
        List<Task> tasks = readHttpTaskManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
        tasks.sort(Comparator.comparing(Task::getId));
        checkAllFieldsTask(task1, tasks.get(0));
        checkAllFieldsTask(task2, tasks.get(1));
        checkAllFieldsTask(task3, tasks.get(2));

        List<Epic> epics = readHttpTaskManager.getListEpic();
        epics.sort(Comparator.comparing(Epic::getId));
        assertEquals(3, epics.size(), "Неверное количество эпиков");
        checkAllFieldsTask(epic1, epics.get(0));
        checkAllFieldsTask(epic2, epics.get(1));
        checkAllFieldsTask(epic3, epics.get(2));

        List<Subtask> subtasks =  readHttpTaskManager.getListSubtaskFromEpic(epic1.getId());
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0));

        subtasks = readHttpTaskManager.getListSubtaskFromEpic(epic2.getId());
        assertEquals(2, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        List<Subtask> expectedSubtask = List.of(subtask2, subtask3);
        assertEquals(expectedSubtask, subtasks, "Подзадачи не совпадают");

        subtasks =  readHttpTaskManager.getListSubtaskFromEpic(epic3.getId());
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        subtasks = readHttpTaskManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        checkAllFieldsTask(subtask1, subtasks.get(0));
        checkAllFieldsTask(subtask2, subtasks.get(1));
        checkAllFieldsTask(subtask3, subtasks.get(2));

        tasks = readHttpTaskManager.getHistory();
        List<Task> expectedTasks = List.of(task1, epic1, epic3, subtask1);
        assertEquals(expectedTasks, tasks, "История не совпадает");

        assertTrue(InMemoryTaskManager.calcNextTaskId()>= SUBTASK_ID3, "Внутренний счётчик не обновился");
    }

    private void testEpicWithoutSubtasks(HttpTaskManager readHttpTaskManager) {
        List<Epic> epics = readHttpTaskManager.getListEpic();
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        checkAllFieldsTask(epic3, epics.get(0));

        List<Subtask>subtasks = readHttpTaskManager.getListSubtaskFromEpic(epic3.getId());
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        //История
        List<Task> tasks = readHttpTaskManager.getHistory();
        assertEquals(epic3, tasks.get(0), "История не совпадает");
    }

    private void testSampleWithoutHistory(HttpTaskManager readHttpTaskManager) {
        List<Task> tasks = readHttpTaskManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
        tasks.sort(Comparator.comparing(Task::getId));
        checkAllFieldsTask(task1, tasks.get(0));
        checkAllFieldsTask(task2, tasks.get(1));
        checkAllFieldsTask(task3, tasks.get(2));

        List<Epic> epics = readHttpTaskManager.getListEpic();
        epics.sort(Comparator.comparing(Epic::getId));
        assertEquals(3, epics.size(), "Неверное количество эпиков");
        checkAllFieldsTask(epic1, epics.get(0));
        checkAllFieldsTask(epic2, epics.get(1));
        checkAllFieldsTask(epic3, epics.get(2));

        List<Subtask> subtasks =  readHttpTaskManager.getListSubtaskFromEpic(epic1.getId());
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0));

        subtasks = readHttpTaskManager.getListSubtaskFromEpic(epic2.getId());
        assertEquals(2, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        List<Subtask> expectedSubtask = List.of(subtask2, subtask3);
        assertEquals(expectedSubtask, subtasks, "Подзадачи не совпадают");

        subtasks =  readHttpTaskManager.getListSubtaskFromEpic(epic3.getId());
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        subtasks = readHttpTaskManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        checkAllFieldsTask(subtask1, subtasks.get(0));
        checkAllFieldsTask(subtask2, subtasks.get(1));
        checkAllFieldsTask(subtask3, subtasks.get(2));

        //Пустая история
        tasks = readHttpTaskManager.getHistory();
        assertTrue(tasks.isEmpty(), "История  должна быть пустой");
    }

}