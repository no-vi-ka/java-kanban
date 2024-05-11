package tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {


    public ArrayList<SubTask> subTasksList;
    public Epic(String title, String description) {
        super(title, description);
        subTasksList = new ArrayList<>();
      }

    public Epic(Epic epic) {
        super(epic);
        ArrayList<SubTask> copiedSubTasksList = new ArrayList<>();
        copiedSubTasksList.addAll(epic.subTasksList);
        this.subTasksList = copiedSubTasksList;
    }

    public void addSubtask(SubTask newSubtask) {
        if (!subTasksList.contains(newSubtask)) {
            subTasksList.add(newSubtask);
        }
    }

    public void updateSubtask(SubTask updatedSubTask) {
        for (int i = 0; i < subTasksList.size(); i++) {
            if (updatedSubTask.equals(subTasksList.get(i))) {
                subTasksList.set(i, updatedSubTask);
            }
        }
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasksList);
    }

    public void removeSubTask(Integer id) {
        subTasksList.removeIf(subTask -> subTask.getId() == id);
    }

    public void removeAllSubTasks() {
        subTasksList.clear();
    }

    public void updateEpicStatus() {
        if (subTasksList.isEmpty()) {
            status = Status.NEW;
            return;
        }
        int doneCounter = 0;
        int newCounter = 0;
        for (SubTask subTask : subTasksList) {
            if (subTask.getStatus() == Status.NEW) {
                newCounter++;
            } else if (subTask.getStatus() == Status.DONE) {
                doneCounter++;
            }
        }
        if (doneCounter == subTasksList.size()) {
            status = Status.DONE;
        } else if (newCounter == subTasksList.size()) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "Epic {" +
                "subtasks = " + subTasksList +
                ", title = '" + getTitle() + '\'' +
                ", description = '" + getDescription() + '\'' +
                ", id = " + getId() +
                ", status = " + status +
                '}';
    }
}
