package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.exception.ManagerSaveException;
import ru.yandex.practicum.tasktracker.mainapp.Main;
import ru.yandex.practicum.tasktracker.task.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file, new InMemoryHistoryManager());
        List<String> lines = fileBackedTasksManager.loadLines();
        boolean hasHistory = false;
        //пропускаем заголовок
        int historyIndex = 0;
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                hasHistory = true;
                historyIndex = i + 1;
                break;
            }
            Task task = fromStringCSV(line);
            switch (task.getType()) {
                case TASK:
                    fileBackedTasksManager.createTask(task);
                    break;
                case EPIC:
                    fileBackedTasksManager.createEpic((Epic)task);
                    break;
                case SUBTASK:
                    fileBackedTasksManager.createSubtask((Subtask) task);
            }
        }
        if (hasHistory && historyIndex < lines.size()) {
            loadHistoryFromStringCSV(fileBackedTasksManager, lines.get(historyIndex));
        }
        return fileBackedTasksManager;
    }

    private void save() {
        final String header = "id,type,name,status,description,epic";
        Path path = file.toPath();
        List<String> lines = new ArrayList<>();
        lines.add(header);
        lines.addAll(convertToCSV(super.getListTask()));
        lines.addAll(convertToCSV(super.getListEpic()));
        lines.addAll(convertToCSV(super.getListSubtask()));
        lines.add("");
        lines.add(historyToStringCSV());
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно записать файл " + path);
        }
    }

    private List<String> loadLines() {
        List<String> lines;
        Path path = file.toPath();
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл " + path);
        }
        return lines;
    }

    private static Task fromStringCSV(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Task task = null;
        String[] split = value.split(",");
        Long id = Long.parseLong(split[0]);
        TaskType type = TaskType.valueOf(split[1]);
        String name = split[2];
        TaskStatus status = TaskStatus.valueOf(split[3]);
        String description = split[4];
        switch (type) {
            case TASK:
                task = new Task(id, name, description,status);
                break;
            case EPIC:
                task = new Epic(id, name, description, status);
                break;
            case SUBTASK:
                Long parentId = Long.parseLong(split[5]);
                task = new Subtask(id, name,description, status, parentId);
        }
        return task;
    }

    private static void loadHistoryFromStringCSV(FileBackedTasksManager fileBackedTasksManager, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        String[] split = value.split(",");
        for (String element : split) {
            Long id = Long.parseLong(element);
            fileBackedTasksManager.getTask(id);
            fileBackedTasksManager.getEpic(id);
            fileBackedTasksManager.getSubtask(id);
        }
    }

    @Override
    public void removeAllTask() {
        super.removeAllTask();
        save();
    }

    @Override
    public Task getTask(Long id) {
        Task task = super.getTask(id);
        if (task != null) {
            save();
        }
        return task;
    }

    @Override
    public boolean createTask(Task task) {
        boolean result = super.createTask(task);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean result = super.updateTask(task);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean removeTask(Long id) {
        boolean result = super.removeTask(id);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public void removeAllEpic() {
        super.removeAllEpic();
        save();
    }

    @Override
    public Epic getEpic(Long id) {
        Epic epic = super.getEpic(id);
        if (epic != null) {
            save();
        }
        return epic;
    }

    @Override
    public boolean createEpic(Epic epic) {
        boolean result = super.createEpic(epic);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean result = super.updateEpic(epic);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean removeEpic(Long id) {
        boolean result = super.removeEpic(id);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public void removeAllSubtask() {
        super.removeAllSubtask();
        save();
    }

    @Override
    public Subtask getSubtask(Long id) {
        Subtask subtask = super.getSubtask(id);
        if (subtask != null) {
            save();
        }
        return subtask;
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        boolean result = super.createSubtask(subtask);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean result = super.updateSubtask(subtask);
        if (result) {
            save();
        }
        return result;
    }

    @Override
    public boolean removeSubtask(Long id) {
        boolean result = super.removeSubtask(id);
        if (result) {
            save();
        }
        return result;
    }

    private List<String> convertToCSV(List<? extends Task> tasks) {
        List<String> list = new ArrayList<>();
        for (var task : tasks) {
            list.add(task.toStringCSV());
        }
        return  list;
    }

    private String historyToStringCSV() {
        List<Task> taskList = super.getHistory();
        List<String> list = new ArrayList<>();
        for (var task : taskList) {
            list.add(task.getId().toString());
        }
        return String.join(",", list);
    }

    public static void main(String[] args) {
        final String PATH = "resources" + File.separator + "tasks.csv";
        final File file = new File(PATH);
        //Заполняем файл
        testFinalSprint5_1(new FileBackedTasksManager(file, new InMemoryHistoryManager()));
        //Читаем файл
        testFinalSprint5_2(loadFromFile(file));
    }

    private static void testFinalSprint5_1(FileBackedTasksManager taskManager) {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ 5.1");
        System.out.println("-------------------");
        taskManager.removeAllEpic();
        taskManager.removeAllTask();

        Task task1 = new Task(null, "задача коробки", "Найти коробки", TaskStatus.IN_PROGRESS);
        Task task2 = new Task(null, "задача вещи", "Собрать вещи", TaskStatus.DONE);
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic(null, "Эпик Спринт 4", "Завершить спринт 4", TaskStatus.NEW);
        Epic epic2 = new Epic(null, "Эпик Спринт 5", "Доделать спринт 5", TaskStatus.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListEpic();
        long idEpic1 = epics.get(0).getId();
        long idEpic2 = epics.get(1).getId();

        Subtask subtask1 = new Subtask(null, "Подзадача 1.1", "Выучить теорию 1", TaskStatus.DONE, idEpic1);
        Subtask subtask2 = new Subtask(null, "Подзадача 1.2", "просто подзадача 1.1", TaskStatus.IN_PROGRESS, idEpic1);
        Subtask subtask3 = new Subtask(null, "Подзадача 5", "Сдать проект 5", TaskStatus.NEW, idEpic2);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        List<Task> tasks = taskManager.getListTask();
        System.out.println("\nСписок задач: " + tasks);
        long idTask1 = tasks.get(0).getId();
        long idTask2 = tasks.get(1).getId();
        System.out.println("Задача1 " + taskManager.getTask(idTask1));
        System.out.println("Задача2 " + taskManager.getTask(idTask2));
        System.out.println("\nСписок эпиков: " + epics);
        System.out.println("Эпик 2 " + taskManager.getEpic(idEpic2));
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        System.out.println("Эпик 1 " + taskManager.getEpic(idEpic1));
        List<Subtask> subtasks = taskManager.getListSubtask();
        long subtaskId1 = subtasks.get(0).getId();
        long subtaskId2 = subtasks.get(1).getId();
        long subtaskId3 = subtasks.get(2).getId();
        System.out.println("\nСписок подзадач: " + subtasks);
        System.out.println("подзадача3 " + taskManager.getSubtask(subtaskId3));
        System.out.println("подзадача1 " + taskManager.getSubtask(subtaskId1));
        System.out.println("подзадача2 " + taskManager.getSubtask(subtaskId2));
        taskManager.removeEpic(idEpic2);
        System.out.println("\n========В ФАЙЛ========");
        Main.printHistory(taskManager.getHistory());
        System.out.println("Задачи: " + taskManager.getListTask());
        System.out.println("Подзадачи: " + taskManager.getListSubtask());
        System.out.println("Эпики: " + taskManager.getListEpic());
    }

    private static void testFinalSprint5_2(TaskManager taskManager) {
        System.out.println("-------------------");
        System.out.println("Финальный тест из ТЗ 5.2");
        System.out.println("-------------------");

        System.out.println("\n========ИЗ ФАЙЛА========");
        Main.printHistory(taskManager.getHistory());
        System.out.println("Задачи: " + taskManager.getListTask());
        System.out.println("Подзадачи: " + taskManager.getListSubtask());
        System.out.println("Эпики: " + taskManager.getListEpic());
    }

}
