package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;

import java.util.List;

public interface TaskManager {

    //Задачи---------------------------------------------------------
    List<Task> getListTask();

    void removeAllTask();

    Task getTask(Long id);

    boolean createTask(Task task);

    boolean updateTask(Task task);

    boolean removeTask(Long id);

    //Эпики----------------------------------------------------------
    List<Epic> getListEpic();

    void removeAllEpic();

    Epic getEpic(Long id);

    boolean createEpic(Epic epic);

    boolean updateEpic(Epic epic);

    boolean  removeEpic(Long id);

    List<Subtask> getListSubtaskFromEpic(Long id);

    //Подзадачи------------------------------------------------------
    List<Subtask> getListSubtask();

    void removeAllSubtask();

    Subtask getSubtask(Long id);

    boolean createSubtask(Subtask subtask);

    boolean updateSubtask(Subtask subtask);

    boolean removeSubtask(Long id);

    //История--------------------------------------------------------
    List<Task> getHistory();
}
