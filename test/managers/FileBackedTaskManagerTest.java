package managers;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest extends TaskManagerTest {


    @Test
    void CreatedEmptyFileShouldBeEmpty() {
        String separator = System.lineSeparator();
        try {
            File file = File.createTempFile("test", "csv");
            String[] lines = Files.readString(file.toPath()).split(separator);
            assertEquals(lines.length, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void EmptyFileShouldNotContainTasks() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
            assertEquals(fileManager.getAllTasks().size(), 0);
            assertEquals(fileManager.getAllEpics().size(), 0);
            assertEquals(fileManager.getAllSubTasks().size(), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldLoadNoTasksFromEmptyFile() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager fileManager = FileBackedTaskManager.loadFromFile(file);
            assertEquals(fileManager.getAllSubTasks().size(), 0);
            assertEquals(fileManager.getAllTasks().size(), 0);
            assertEquals(fileManager.getAllEpics().size(), 0);
            assertTrue(fileManager.getAllTasks().isEmpty());
            assertTrue(fileManager.getAllEpics().isEmpty());
            assertTrue(fileManager.getAllSubTasks().isEmpty());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}