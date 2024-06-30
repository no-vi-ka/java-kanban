package tasks;

import java.util.Objects;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task implements Comparable<Task> {
    private String title;
    private String description;
    private Integer id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Task(String title, String description, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Task task) {
        this(task.description, task.title, task.duration, task.startTime);
        this.status = task.status;
        this.id = task.id;
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
        return "Task {" +
                "title = '" + title + '\'' +
                ", description = '" + description + '\'' +
                ", id = " + id +
                ", status = " + status +
                ", duration=" + durationToString + "мин." +
                ", start_time=" + startTimeToString +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public int compareTo(Task t) {
        if (startTime == null || startTime.isAfter(t.getStartTime())) {
            return 1;
        }
        return -1;
    }
}

