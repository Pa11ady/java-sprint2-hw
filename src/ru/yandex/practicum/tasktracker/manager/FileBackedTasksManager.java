package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.mainapp.Main;
import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        return new FileBackedTasksManager(file);
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
            System.out.println("Невозможно прочитать файл " + path);
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
        save();
        return task;
    }

    @Override
    public boolean createTask(Task task) {
        boolean result = super.createTask(task);
        save();
        return result;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean result = super.updateTask(task);
        save();
        return result;
    }

    @Override
    public boolean removeTask(Long id) {
        boolean result = super.removeTask(id);
        save();
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
        save();
        return epic;
    }

    @Override
    public boolean createEpic(Epic epic) {
        boolean result = super.createEpic(epic);
        save();
        return result;
    }

    @Override
    public boolean updateEpic(Epic epic) {
        boolean result = super.updateEpic(epic);
        save();
        return result;
    }

    @Override
    public boolean removeEpic(Long id) {
        boolean result = super.removeEpic(id);
        save();
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
        save();
        return subtask;
    }

    @Override
    public boolean createSubtask(Subtask subtask) {
        boolean result = super.createSubtask(subtask);
        save();
        return result;
    }

    @Override
    public boolean updateSubtask(Subtask subtask) {
        boolean result = super.updateSubtask(subtask);
        save();
        return result;
    }

    @Override
    public boolean removeSubtask(Long id) {
        boolean result = super.removeSubtask(id);
        save();
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
        System.out.println("File");
        //Main.testFinalSprint3(loadFromFile(null));
        Main.testFinalSprint4(loadFromFile(file));

    }



}
