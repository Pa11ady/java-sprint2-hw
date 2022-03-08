package ru.yandex.practicum.tasktracker.manager;

import ru.yandex.practicum.tasktracker.task.Epic;
import ru.yandex.practicum.tasktracker.task.Subtask;
import ru.yandex.practicum.tasktracker.task.Task;

import java.util.List;

public interface TaskManager {

    //Задачи---------------------------------------------------------
    List<Task> getListTask();

    void removeAllTask();

    Task getTask(long id);

    boolean createTask(Task task);

    boolean updateTask(Task task);

    boolean removeTask(long id);

    //Эпики----------------------------------------------------------
    List<Epic> getListEpic();

    void removeAllEpic();

    Epic getEpic(long id);

    boolean createEpic(Epic epic);

    boolean updateEpic(Epic epic);

    boolean  removeEpic(long id);

    List<Subtask> getListSubtaskFromEpic(long id);

    //Подзадачи------------------------------------------------------
    List<Subtask> getListSubtask();

    void removeAllSubtask();

    Subtask getSubtask(Long id);

    boolean createSubtask(Subtask subtask);

    boolean updateSubtask(Subtask subtask);

    boolean removeSubtask(long id);
}
