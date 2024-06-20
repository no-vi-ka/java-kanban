package managers;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;
import exceptions.ManagerSaveException;

import java.io.*;
import java.util.List;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    static String firstString = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File file) throws IOException {
        this.file = file;
        save();
    }

    @Override
    public Task createTask(Task newTask) {
        if (newTask != null) {
            save();
            return super.createTask(newTask);
        } else {
            return null;
        }

    }

    @Override
    public SubTask createSubTask(SubTask newSubTask) {
        if (newSubTask != null) {
            save();
            return super.createSubTask(newSubTask);
        } else {
            return null;
        }
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        if (newEpic != null) {
            save();
            return super.createEpic(newEpic);
        } else {
            return null;
        }
    }

    @Override
    public void updateTask(Task updatedTask) {
        save();
        super.updateTask(updatedTask);
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        save();
        super.updateSubTask(updatedSubTask);
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        save();
        super.updateEpic(updatedEpic);
    }

    @Override
    public List<SubTask> getAllSubtasksOfEpic(Epic epic) {
        save();
        return super.getAllSubtasksOfEpic(epic);
    }

    @Override
    public void removeAllTasks() {
        save();
        super.removeAllTasks();
    }

    @Override
    public void removeAllEpics() {
        save();
        super.removeAllEpics();
    }

    @Override
    public void removeAllSubTasks() {
        save();
        super.removeAllSubTasks();
    }

    @Override
    public void removeTaskById(Integer id) {
        save();
        super.removeTaskById(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        save();
        super.removeEpicById(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        save();
        super.removeSubtaskById(id);
    }

    @Override
    public Task getTaskById(Integer id) {
        return super.getTaskById(id);
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        return super.getSubTaskById(id);
    }

    @Override
    public Epic getEpicById(Integer id) {
        return super.getEpicById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return super.getAllSubTasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public List<SubTask> getAllSubTaskByEpicId(Integer id) {
        return super.getAllSubTaskByEpicId(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public void save() {
        String fileName = "fileTaskManager.csv";
        try (Writer fileWriter = new FileWriter(fileName, StandardCharsets.UTF_8)) {
            fileWriter.write(firstString + "\n");
            for (Task task : super.tasks.values()) {
                String converted = toString(task);
                fileWriter.write(converted + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка ввода");
        }
    }

    private TaskTypes getType(Task task) {
        if (task instanceof Epic) {
            return TaskTypes.EPIC;
        } else if (task instanceof SubTask) {
            return TaskTypes.SUBTASK;
        }
        return TaskTypes.TASK;
    }

    public String toString(Task task) {
        if (task.getClass() == Task.class) {
            return (task.getId() + "," + getType(task).toString() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription());

        } else if (task.getClass() == Epic.class) {
            return (task.getId() + "," + getType(task).toString() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription());
        } else {
            SubTask subTask = (SubTask) task;
            return (subTask.getId() + "," + getType(task).toString() + "," + subTask.getTitle() + "," + subTask.getStatus() + "," + subTask.getDescription() + "," + subTask.getEpicId());
        }
    }

    public static Task fromString(String value) {
        String[] splittedString = value.split(",");
        int id = Integer.parseInt(splittedString[0]);
        TaskTypes taskType = TaskTypes.valueOf(splittedString[1]);
        String title = splittedString[2];
        Status taskStatus = Status.valueOf(splittedString[3]);
        String description = splittedString[4];
        Task task = null;
        if (splittedString.length == 6) {
            int epicId = Integer.parseInt(splittedString[5]);
            task = new SubTask(title, description, epicId);
            task.setId(id);
            task.setStatus(taskStatus);
            return task;
        } else {
            if (taskType == TaskTypes.TASK) {
                task = new Task(title, description);
                task.setId(id);
                task.setStatus(taskStatus);
                return task;
            } else {
                task = new Epic(title, description);
                task.setId(id);
                task.setStatus(taskStatus);
                return task;
            }
        }
    }

    private static TaskTypes toEnum(Task task) {
        if (task instanceof Epic) {
            return TaskTypes.EPIC;
        } else if (task instanceof SubTask) {
            return TaskTypes.SUBTASK;
        }
        return TaskTypes.TASK;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            String separator = System.lineSeparator();
            String[] separatedStrings = Files.readString(file.toPath()).split(separator);
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            for (String string : separatedStrings) {
                if (string.equals(firstString) || string.isBlank()) {
                    continue;
                }
                Task task = fromString(string);
                TaskTypes typeOfTask = toEnum(task);

                switch (typeOfTask) {
                    case TASK -> fileBackedTaskManager.createTask(task);
                    case EPIC -> fileBackedTaskManager.createEpic((Epic) task);
                    case SUBTASK -> fileBackedTaskManager.createSubTask((SubTask) task);
                    default -> throw new ManagerSaveException("Ошибка при определении типа задачи");
                }
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка восстановления данных");
        }
    }
}
