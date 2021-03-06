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

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    final static String PATH = "resources" + File.separator + "tasks_tmp.csv";

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

    private void testSampleStandard(FileBackedTasksManager readFileBackedTasksManager) {
        List<Task> tasks = readFileBackedTasksManager.getListTask();
        assertEquals(3, tasks.size(), "Неверное количество задач");
        tasks.sort(Comparator.comparing(Task::getId));
        checkAllFieldsTask(task1, tasks.get(0));
        checkAllFieldsTask(task2, tasks.get(1));
        checkAllFieldsTask(task3, tasks.get(2));

        List<Epic> epics = readFileBackedTasksManager.getListEpic();
        epics.sort(Comparator.comparing(Epic::getId));
        assertEquals(3, epics.size(), "Неверное количество эпиков");
        checkAllFieldsTask(epic1, epics.get(0));
        checkAllFieldsTask(epic2, epics.get(1));
        checkAllFieldsTask(epic3, epics.get(2));

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
        checkAllFieldsTask(subtask1, subtasks.get(0));
        checkAllFieldsTask(subtask2, subtasks.get(1));
        checkAllFieldsTask(subtask3, subtasks.get(2));

        tasks = readFileBackedTasksManager.getHistory();
        List<Task> expectedTasks = List.of(task1, epic1, epic3, subtask1);
        assertEquals(expectedTasks, tasks, "История не совпадает");

        assertTrue(InMemoryTaskManager.calcNextTaskId()>= SUBTASK_ID3, "Внутренний счётчик не обновился");
    }

    private void testEpicWithoutSubtasks(FileBackedTasksManager readFileBackedTasksManager) {
        List<Epic> epics = readFileBackedTasksManager.getListEpic();
        assertEquals(1, epics.size(), "Неверное количество эпиков");
        checkAllFieldsTask(epic3, epics.get(0));

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
        checkAllFieldsTask(task1, tasks.get(0));
        checkAllFieldsTask(task2, tasks.get(1));
        checkAllFieldsTask(task3, tasks.get(2));

        List<Epic> epics = readFileBackedTasksManager.getListEpic();
        epics.sort(Comparator.comparing(Epic::getId));
        assertEquals(3, epics.size(), "Неверное количество эпиков");
        checkAllFieldsTask(epic1, epics.get(0));
        checkAllFieldsTask(epic2, epics.get(1));
        checkAllFieldsTask(epic3, epics.get(2));

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
        checkAllFieldsTask(subtask1, subtasks.get(0));
        checkAllFieldsTask(subtask2, subtasks.get(1));
        checkAllFieldsTask(subtask3, subtasks.get(2));

        //Пустая история
        tasks = readFileBackedTasksManager.getHistory();
        assertTrue(tasks.isEmpty(), "История  должна быть пустой");
    }


}