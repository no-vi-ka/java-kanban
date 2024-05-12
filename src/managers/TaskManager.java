package managers;

import tasks.Task;
import tasks.SubTask;
import tasks.Epic;
import tasks.Status;

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

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    List<SubTask> getAllSubTaskByEpicId(Integer id);

    List<SubTask> getAllSubtasksOfEpic(Epic epic);

    List<Task> getHistory();
}
