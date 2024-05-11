package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> tasksHistory = new ArrayList<>();
    private final static int MAX_HISTORY_SIZE = 10;

    @Override
    public void addTask(Task task) {

    }

    public void addToHistory(Task task) {
        if (task != null) {
            tasksHistory.add(task);
            if (tasksHistory.size() >= MAX_HISTORY_SIZE) {
                tasksHistory.removeFirst();
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        for (Task task: tasksHistory) {
            historyList.add(new Task(task));
        }
        return historyList;
    }
}
