package tasks;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String title, String description, Integer epicId) {
        super(title, description);
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
        return "Subtask { " +
                "epicId = " + epicId +
                ", title = '" + getTitle() + '\'' +
                ", description = '" + getDescription() + '\'' +
                ", id = " + getId() +
                ", status = " + status +
                '}';
    }
}

