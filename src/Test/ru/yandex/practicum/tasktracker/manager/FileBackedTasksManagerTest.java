package ru.yandex.practicum.tasktracker.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>  {

    final static File file = new File("resources" + File.separator + "tasks_tmp.csv");
    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(file));
    }

    @BeforeEach
    void setUp() {
        taskManager.removeAllTask();
        taskManager.removeAllEpic();
    }

    @Test
    void loadFromFile() {
    }
}