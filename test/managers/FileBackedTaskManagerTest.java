package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {


    @Test
    void CreatedEmptyFileShouldBeEmpty() {
        String separator = System.lineSeparator();
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

            String[] lines = Files.readString(file.toPath()).split(separator);
            Assertions.assertEquals(lines.length, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void EmptyFileShouldNotContainTasks() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
            Assertions.assertEquals(fileManager.getAllTasks().size(), 0);
            Assertions.assertEquals(fileManager.getAllEpics().size(), 0);
            Assertions.assertEquals(fileManager.getAllSubTasks().size(), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void savingTasksTest() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);

            Task task1 = new Task("Задача 1", "Описание 1");
            fileManager.createTask(task1);
            Task task2 = new Task("Задача 2", "Описание 2");
            fileManager.createTask(task2);
            Epic epic1 = new Epic("Эпик1", "Описание 1");
            fileManager.createEpic(epic1);
            SubTask subtask1 = new SubTask("Подзадача 1", "...", epic1.getId());
            SubTask subtask2 = new SubTask("Подзадача 2", "...", epic1.getId());
            fileManager.createSubTask(subtask1);
            fileManager.createSubTask(subtask2);

            Assertions.assertEquals(fileManager.getAllTasks().size(), 2, "Количество задач не совпадает");
            Assertions.assertEquals(fileManager.getAllEpics().size(), 1, "Количество эпиков не совпадает");
            Assertions.assertEquals(fileManager.getAllSubTasks().size(), 2, "Количество подзадач не совпадает");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}