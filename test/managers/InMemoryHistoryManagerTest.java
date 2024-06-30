package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
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
        Task newTask = new Task("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        manager.createTask(newTask);
        historyManager.addTask(newTask);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void sizeOfHistoryListMustBeEqualsNumberOfAddedTasks() {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Task task2 = new Task("a2", "b2", Duration.ofMinutes(30), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20));
        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        historyManager.addTask(newTask1);
        historyManager.addTask(newTask2);
        assertEquals(2, historyManager.getHistory().size());
    }

    @Test
    public void historyShouldContainsUniqueRecords() {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Task task2 = new Task("a2", "b2", Duration.ofMinutes(30), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20));
        Task task3 = new Task("a3", "b3", Duration.ofMinutes(30), LocalDateTime.of(2030, Month.AUGUST,
                10, 10, 10));
        Task task4 = new Task("a4", "b4", Duration.ofMinutes(40), LocalDateTime.of(2040, Month.SEPTEMBER,
                20, 20, 20));

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
    public void historyShouldContainsTheLastVersionOfRecord() {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Task task2 = new Task("a2", "b2", Duration.ofMinutes(30), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20));
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