package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String title, String description, Duration duration, LocalDateTime startTime, Integer epicId) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(SubTask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
    }

    public Integer getEpicId() {
        return epicId;
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
        return "Subtask { " +
                "epicId = " + epicId +
                ", title = '" + getTitle() + '\'' +
                ", description = '" + getDescription() + '\'' +
                ", id = " + getId() +
                ", status = " + status +
                ", duration=" + durationToString + "мин." +
                ", start_time=" + startTimeToString +
                '}';
    }
}

