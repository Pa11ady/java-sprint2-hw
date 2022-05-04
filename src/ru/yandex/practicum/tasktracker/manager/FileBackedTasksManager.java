package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.enums.TaskType;
import ru.yandex.practicum.tasktracker.enums.TaskStatus;
import ru.yandex.practicum.tasktracker.exception.ManagerSaveException;
import ru.yandex.practicum.tasktracker.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

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
        final String header = "id,type,name,status,description,duration,epic,startTime";
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
        Integer duration = split[5].isBlank() ? null : Integer.parseInt(split[5]);
        LocalDateTime startTime = split[6].isBlank() ? null : LocalDateTime.parse(split[6]);
        switch (type) {
            case TASK:
                task = new Task(id, name, description, status,  duration,  startTime);
                break;
            case EPIC:
                task = new Epic(id, name, description, status,  duration,  startTime);
                break;
            case SUBTASK:
                Long parentId = Long.parseLong(split[7]);
                task = new Subtask(id, name, description, status, parentId, duration,  startTime);
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
}
