package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    Task createTask(Task newTask);

    SubTask createSubTask(SubTask newSubTask);

    Epic createEpic(Epic newEpic);

    void updateTask(Task updatedTask);

    void updateSubTask(SubTask updatedSubTask);

    void updateEpic(Epic updatedEpic);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeTaskById(Integer id);

    void removeEpicById(Integer id);
    void removeSubtaskById(Integer id);

    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    SubTask getSubTaskById(Integer id);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubTasks();

    ArrayList<SubTask> getAllSubTaskByEpicId(Integer id);

    ArrayList<SubTask> getAllSubtasksOfEpic(Epic epic);

    List<Task> getHistory();
}
