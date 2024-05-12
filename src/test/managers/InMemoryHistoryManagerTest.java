package managers;

import static org.junit.jupiter.api.Assertions.*;

import tasks.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class InMemoryHistoryManagerTest {
    private TaskManager manager;
    private HistoryManager historyManager;

    @BeforeEach
    public void init() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void historyListMustBeNotNullAfterTasksWereAdded() {
        Task newTask = new Task("a", "b");
        historyManager.addTask(newTask);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void sizeOfHistoryListMustBeEqualsNumberOfAddedTasks(){
        Task task1 = new Task("a", "b");
        Task task2 = new Task("a1", "b1");
        historyManager.addTask(task2);
        historyManager.addTask(task1);
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
}