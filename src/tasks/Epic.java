package tasks;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

public class Epic extends Task {

    public List<SubTask> subTasksList;
    private LocalDateTime endTime;

    public Epic(String title, String description, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        subTasksList = new ArrayList<>();
        this.endTime = startTime.plus(duration);
    }

    public Epic(Epic epic) {
        super(epic);
        ArrayList<SubTask> copiedSubTasksList = new ArrayList<>();
        copiedSubTasksList.addAll(epic.subTasksList);
        this.subTasksList = deepCopyArrayList(epic);
    }

    private List<SubTask> deepCopyArrayList(Epic epic) {
        return epic.subTasksList.stream().toList();
    }

    public void addSubtask(SubTask newSubtask) {
        if (!subTasksList.contains(newSubtask)) {
            subTasksList.add(newSubtask);
            if (newSubtask.getStartTime().isBefore(getStartTime()) || getStartTime() == null) {
                setStartTime(newSubtask.getStartTime());
            }
            if (newSubtask.getEndTime().isAfter(getEndTime()) || getEndTime() == null) {
                setEndTime(newSubtask.getEndTime());
            }
        }
    }

    public void updateSubtask(SubTask updatedSubTask) {
        for (int i = 0; i < subTasksList.size(); i++) {
            if (updatedSubTask.equals(subTasksList.get(i))) {
                subTasksList.set(i, updatedSubTask);
            }
        }
    }

    public List<SubTask> getSubTasks() {
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        String startTimeToString;
        String durationToString;
        if (startTime != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            startTimeToString = startTime.format(dateTimeFormatter);
        } else {
            startTimeToString = "время начала не задано";
        }
        if (duration != null) {
            durationToString = String.valueOf(duration.toMinutes());
        } else {
            durationToString = "продолжительность не задана";
        }
        return "Epic {" +
                "subtasks = " + subTasksList +
                "title = '" + getTitle() + '\'' +
                ", description = '" + getDescription() + '\'' +
                ", id = " + getId() +
                ", status = " + status +
                ", duration=" + durationToString + "мин." +
                ", start_time=" + startTimeToString +
                '}';
    }
}

