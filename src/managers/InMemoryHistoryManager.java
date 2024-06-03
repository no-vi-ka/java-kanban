package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head = null;
    private Node<Task> tail = null;
    private final Map<Integer, Node<Task>> tasksHistory = new HashMap<>();

    @Override
    public void addTask(Task task) {
        if (task != null) {
            if (tasksHistory.get(task.getId()) != null) {
                remove(task.getId());
            }
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        if (tasksHistory.containsKey(id)) {
            Node<Task> nodeToDelete = tasksHistory.get(id);
            if (nodeToDelete == head) {
                head = nodeToDelete.getNext();
            } else {
                nodeToDelete.getPrev().setNext(nodeToDelete.getNext());
            }
            if (nodeToDelete == tail) {
                tail = nodeToDelete.getPrev();
            } else {
                nodeToDelete.getNext().setPrev(nodeToDelete.getPrev());
            }
            tasksHistory.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    public void linkLast(Task task) {
        final Node<Task> newNode = new Node<>(null, task, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(head);
        }
        tail = newNode;
        tasksHistory.put(tail.getTask().getId(), tail);
    }

    public List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> curNode = head;
        while (curNode != null) {
            historyList.add(curNode.getTask());
            curNode = curNode.getNext();
        }
        return historyList;
    }
}



