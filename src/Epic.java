import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description);
      }

    public void addSubtask(SubTask newSubtask) {
        subTasks.putIfAbsent(newSubtask.getId(), newSubtask);
    }

    public void updateSubtask(SubTask updatedSubTask) {
        if (subTasks.containsKey(updatedSubTask.getId())) {
            subTasks.put(updatedSubTask.getId(), updatedSubTask);
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void removeSubTask(Integer id) {
        subTasks.remove(id);
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    void updateEpicStatus() {
        if (subTasks.isEmpty()) {
            status = Status.NEW;
            return;
        }
        int doneCounter = 0;
        int newCounter = 0;
        for (SubTask subTask: subTasks.values()) {
            if (subTask.getStatus() == Status.NEW) {
                newCounter++;
            } else if (subTask.getStatus() == Status.DONE) {
                doneCounter++;
            }
        }
        if (doneCounter == subTasks.size()) {
            status = Status.DONE;
        } else if (newCounter == subTasks.size()) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic {" +
                "subtasks = " + subTasks +
                ", title = '" + title + '\'' +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", status = " + status +
                '}';
    }
}
