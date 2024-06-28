package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeTest {
    private TaskManager manager;
    private HistoryManager historyManager;

    @BeforeEach
    public void init() {
        manager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void getTaskShouldReturnTask() {
        Task task1 = new Task("a1", "b1");
        Task newTask1 = manager.createTask(task1);
        Node<Task> newNode = new Node<>(null, newTask1, null);
        assertEquals(newTask1, newNode.getTask());
    }

    @Test
    void getNextShouldReturnNextTask() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        historyManager.addTask(newTask1);
        historyManager.addTask(newTask2);
        assertEquals(newTask2, historyManager.getHistory().getLast());
    }

    @Test
    void getPrevShouldReturnPrevTask() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        historyManager.addTask(newTask1);
        historyManager.addTask(newTask2);
        assertEquals(newTask1, historyManager.getHistory().getFirst());
    }

    @Test
    void setNextShouldSetNextNode() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        Node<Task> node1 = new Node<>(null, newTask1, null);
        Node<Task> node2 = new Node<>(null, newTask2, null);
        node1.setNext(node2);
        assertEquals(node2, node1.getNext());
    }

    @Test
    void setPrevShouldSetPrevNode() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        Task newTask1 = manager.createTask(task1);
        Task newTask2 = manager.createTask(task2);
        Node<Task> node1 = new Node<>(null, newTask1, null);
        Node<Task> node2 = new Node<>(null, newTask2, null);
        node2.setPrev(node1);
        assertEquals(node1, node2.getPrev());
    }
}