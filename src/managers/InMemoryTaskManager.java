package managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import tasks.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    @Override
    public Task createTask(Task newTask) {
        int id = nextId++;
        newTask.setId(id);
        tasks.put(newTask.getId(), newTask);
        return newTask;
    }

    @Override
    public SubTask createSubTask(SubTask newSubTask) {
        if (epics.containsKey(newSubTask.getEpicId())) {
            int id = nextId++;
            newSubTask.setId(id);
            subTasks.put(newSubTask.getId(), newSubTask);
            int index = newSubTask.getEpicId();
            Epic epic = epics.get(index);
            epic.addSubtask(newSubTask);
            epic.updateEpicStatus();
        }
        return newSubTask;
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        int id = nextId++;
        newEpic.setId(id);
        epics.put(newEpic.getId(), newEpic);
        return newEpic;
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
        }
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        if (subTasks.containsKey(updatedSubTask.getId()) && epics.containsKey(updatedSubTask.getEpicId())) {
            subTasks.put(updatedSubTask.getId(), updatedSubTask);
            Epic epic = epics.get(updatedSubTask.getEpicId());
            epic.updateSubtask(updatedSubTask);
            epic.updateEpicStatus();
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            Epic epic = epics.get(updatedEpic.getId());
            epic.setTitle(updatedEpic.getTitle());
            epic.setDescription(updatedEpic.getDescription());
        }
    }

    @Override
    public List<SubTask> getAllSubtasksOfEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epic.getSubTasks();
        }
        return new ArrayList<>();
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void removeAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.subTasksList.clear();
            epic.updateEpicStatus();
        }
        subTasks.clear();
    }

    @Override
    public void removeTaskById(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        if (epics.containsKey(id)) {
            for (SubTask subtask : epics.get(id).getSubTasks()) {
                subTasks.remove(subtask.getId());
            }
            epics.get(id).getSubTasks().clear();
            epics.remove(id);
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        historyManager.addTask(task);
        return tasks.get(id);

    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask subtask = subTasks.get(id);
        historyManager.addTask(subtask);
        return subTasks.get(id);
    }
    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epics.get(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            Epic epic = getEpicById(subTasks.get(id).getEpicId());
            SubTask subTaskToRemove = subTasks.get(id);
            epic.subTasksList.remove(subTaskToRemove);
            epic.updateEpicStatus();
            subTasks.remove(id);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTaskByEpicId(Integer id) {
        Epic epicToShowSubTasks = epics.get(id);
        return epicToShowSubTasks.subTasksList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
