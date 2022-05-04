package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>  {
    final static  String PATH = "resources" + File.separator + "tasks_tmp.csv";

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File(PATH)));
    }

    @BeforeEach
    void setUp() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
    }

    @Test
    void loadFromFile() {
        File file = new File(PATH);
        FileBackedTasksManager readFileBackedTasksManager;
        List<Task> tasks;
        List<Epic> epics;
        List<Subtask> subtasks;

        //Пустой список задач
        readFileBackedTasksManager =  FileBackedTasksManager.loadFromFile(file);
        tasks = readFileBackedTasksManager.getListTask();
        assertTrue(tasks.isEmpty(), "Список Задач должен быть пустой");

        epics = readFileBackedTasksManager.getListEpic();
        assertTrue(epics.isEmpty(), "Список Эпиков должен быть пустой");

        subtasks = readFileBackedTasksManager.getListSubtask();
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        tasks = readFileBackedTasksManager.getHistory();
        assertTrue(tasks.isEmpty(), "Список  должен быть пустой");

        //Стандартное поведение
        createSampleStandard();
        readFileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        testSampleStandard(readFileBackedTasksManager);

        taskManager.removeAllTask();
        taskManager.removeAllEpic();

        //Эпик без подзадач
        taskManager.createEpic(epic3);
        taskManager.getEpic(epic3.getId());
        readFileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        testEpicWithoutSubtasks(readFileBackedTasksManager);

        taskManager.removeAllTask();
        taskManager.removeAllEpic();

        //Без истории
        createSampleWithoutHistory();
        readFileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        testSampleWithoutHistory(readFileBackedTasksManager);
    }

    private void createSampleStandard() {
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

    private void createSampleWithoutHistory() {
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

    private void testSampleStandard(FileBackedTasksManager readFileBackedTasksManager) {
        List<Task> tasks = readFileBackedTasksManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
        tasks.sort(Comparator.comparing(Task::getId));
        testAllFieldsTask(task1, tasks.get(0));
        testAllFieldsTask(task2, tasks.get(1));
        testAllFieldsTask(task3, tasks.get(2));

        List<Epic> epics = readFileBackedTasksManager.getListEpic();
        epics.sort(Comparator.comparing(Epic::getId));
        assertEquals(3, epics.size(), "Неверное количество эпиков");
        testAllFieldsTask(epic1, epics.get(0));
        testAllFieldsTask(epic2, epics.get(1));
        testAllFieldsTask(epic3, epics.get(2));

        List<Subtask> subtasks =  readFileBackedTasksManager.getListSubtaskFromEpic(epic1.getId());
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0));

        subtasks = readFileBackedTasksManager.getListSubtaskFromEpic(epic2.getId());
        assertEquals(2, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        List<Subtask> expectedSubtask = List.of(subtask2, subtask3);
        assertEquals(expectedSubtask, subtasks, "Подзадачи не совпадают");

        subtasks =  readFileBackedTasksManager.getListSubtaskFromEpic(epic3.getId());
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        subtasks = readFileBackedTasksManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        testAllFieldsTask(subtask1, subtasks.get(0));
        testAllFieldsTask(subtask2, subtasks.get(1));
        testAllFieldsTask(subtask3, subtasks.get(2));

        tasks = readFileBackedTasksManager.getHistory();
        List<Task> expectedTasks = List.of(task1, epic1, epic3, subtask1);
        assertEquals(expectedTasks, tasks, "История не совпадает");

        assertTrue(InMemoryTaskManager.calcNextTaskId()>= SUBTASK_ID3, "Внутренний счётчик не обновился");
    }

    private void testEpicWithoutSubtasks(FileBackedTasksManager readFileBackedTasksManager) {
        List<Epic> epics = readFileBackedTasksManager.getListEpic();
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        testAllFieldsTask(epic3, epics.get(0));

        List<Subtask>subtasks = readFileBackedTasksManager.getListSubtaskFromEpic(epic3.getId());
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        //История
        List<Task> tasks = readFileBackedTasksManager.getHistory();
        assertEquals(epic3, tasks.get(0), "История не совпадает");
    }

    private void testSampleWithoutHistory(FileBackedTasksManager readFileBackedTasksManager) {
        List<Task> tasks = readFileBackedTasksManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
        tasks.sort(Comparator.comparing(Task::getId));
        testAllFieldsTask(task1, tasks.get(0));
        testAllFieldsTask(task2, tasks.get(1));
        testAllFieldsTask(task3, tasks.get(2));

        List<Epic> epics = readFileBackedTasksManager.getListEpic();
        epics.sort(Comparator.comparing(Epic::getId));
        assertEquals(3, epics.size(), "Неверное количество эпиков");
        testAllFieldsTask(epic1, epics.get(0));
        testAllFieldsTask(epic2, epics.get(1));
        testAllFieldsTask(epic3, epics.get(2));

        List<Subtask> subtasks =  readFileBackedTasksManager.getListSubtaskFromEpic(epic1.getId());
        assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        assertEquals(subtask1, subtasks.get(0));

        subtasks = readFileBackedTasksManager.getListSubtaskFromEpic(epic2.getId());
        assertEquals(2, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        List<Subtask> expectedSubtask = List.of(subtask2, subtask3);
        assertEquals(expectedSubtask, subtasks, "Подзадачи не совпадают");

        subtasks =  readFileBackedTasksManager.getListSubtaskFromEpic(epic3.getId());
        assertTrue(subtasks.isEmpty(), "Список Подзадач должен быть пустой");

        subtasks = readFileBackedTasksManager.getListSubtask();
        assertEquals(3, subtasks.size(), "Неверное количество Подзадач");
        subtasks.sort(Comparator.comparing(Subtask::getId));
        testAllFieldsTask(subtask1, subtasks.get(0));
        testAllFieldsTask(subtask2, subtasks.get(1));
        testAllFieldsTask(subtask3, subtasks.get(2));

        //Пустая история
        tasks = readFileBackedTasksManager.getHistory();
        assertTrue(tasks.isEmpty(), "История  должна быть пустой");
    }
}