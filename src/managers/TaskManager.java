package managers;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import tasks.*;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();

    private int nextId = 1;

    private Integer getNewId() {
        return nextId++;
    }


    public Task createTask(Task newTask) {
        newTask.setId(getNewId());
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    public SubTask createSubTask(SubTask newSubTask) {
        if (epics.containsKey(newSubTask.getEpicId())) {
            newSubTask.setId(getNewId());
            subTasks.put(newSubTask.getId(), newSubTask);
            int index = newSubTask.getEpicId();
            Epic epic = epics.get(index);
            epic.addSubtask(newSubTask);
            epic.updateEpicStatus();
        }
        return newSubTask;
    }

    public Epic createEpic(Epic newEpic) {
        newEpic.setId(getNewId());
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    public void updateSubTask(SubTask updatedSubTask) {
        if (subTasks.containsKey(updatedSubTask.getId()) && epics.containsKey(updatedSubTask.getEpicId())) {
            subTasks.put(updatedSubTask.getId(), updatedSubTask);
            Epic epic = epics.get(updatedSubTask.getEpicId());
            epic.updateSubtask(updatedSubTask);
            epic.updateEpicStatus();
        }
    }

    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            Epic epic = epics.get(updatedEpic.getId());
            epic.setTitle(updatedEpic.getTitle());
            epic.setDescription(updatedEpic.getDescription());
        }
    }

    public ArrayList<SubTask> getAllSubtasksOfEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            return epic.getSubTasks();
        }
        return new ArrayList<>();
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        subTasks.clear();
        epics.clear();
    }

    public void removeAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.subTasksList.clear();
            epic.updateEpicStatus();
        }
        subTasks.clear();
    }

    public void removeTaskById(Integer id) {
        tasks.remove(id);
    }

    public void removeEpicById(Integer id) {
        if (epics.containsKey(id)) {
            for (SubTask subtask : epics.get(id).getSubTasks()) {
                subTasks.remove(subtask.getId());
            }
            epics.get(id).getSubTasks().clear();
            epics.remove(id);
        }
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(Integer id) {
        return subTasks.get(id);
    }
    public Epic getEpicById(Integer id) {
        return epics.get(id);
    }

    public void removeSubtaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            Epic epic = getEpicById(subTasks.get(id).getEpicId());
            SubTask subTaskToRemove = subTasks.get(id);
            epic.subTasksList.remove(subTaskToRemove);
            epic.updateEpicStatus();
            subTasks.remove(id);
        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<SubTask> getAllSubTaskByEpicId(Integer id) {
        Epic epicToShowSubTasks = epics.get(id);
        return epicToShowSubTasks.subTasksList;
    }
}
