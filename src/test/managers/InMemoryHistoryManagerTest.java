package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {
    private TaskManager manager;
    private HistoryManager historyManager;

    @BeforeEach
    public void init() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void historyListMustBeNotEmptyAfterTasksWereAdded() {
        Task newTask = new Task("a", "b");
        historyManager.addTask(newTask);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void sizeOfHistoryListMustBeEqualsNumberOfAddedTasks() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        historyManager.addTask(newTask1);
        historyManager.addTask(newTask2);
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    public void HistoryManagerMustSaveFirstVersionOfTaskAndChangedVersion() {
        Task task = new Task("a", "b");
        Task newTask = manager.createTask(task);
        manager.getTaskById(newTask.getId());
        newTask.setTitle("a1");
        manager.updateTask(newTask);
        Assertions.assertNotEquals(manager.getHistory().getFirst().getTitle(), task.getTitle());
    }

    @Test
    public void HistoryShouldContainsUniqueRecords() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        Task task3 = new Task("a3", "b3");
        Task task4 = new Task("a41", "b4");

        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        Task newTask3 = manager.createTask(task3);
        Task newTask4 = manager.createTask(task4);

        historyManager.addTask(newTask1);
        historyManager.addTask(newTask2);
        historyManager.addTask(newTask3);
        historyManager.addTask(newTask4);
        historyManager.addTask(newTask2);
        historyManager.addTask(newTask3);
        historyManager.addTask(newTask4);

        assertEquals(4, historyManager.getHistory().size());
    }

    @Test
    public void HistoryShouldContainsTheLastVersionOfRecord() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        historyManager.addTask(newTask1);
        historyManager.addTask(newTask2);
        newTask1.setTitle("a3");
        manager.updateTask(newTask1);
        historyManager.addTask(newTask1);
        assertEquals(newTask1, historyManager.getHistory().getLast());
    }
}