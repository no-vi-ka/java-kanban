package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    public final Map<Integer, Task> tasks = new HashMap<>();
    public final Map<Integer, SubTask> subTasks = new HashMap<>();
    public final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;
    private TreeSet<Task> prioritizedTasks = new TreeSet<>();

    @Override
    public Task createTask(Task newTask) {
        int id = nextId++;
        newTask.setId(id);
        tasks.put(newTask.getId(), newTask);
        updatePrioritizedTasks();
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
            if (epic.getStartTime() == null || epic.getStartTime().isAfter(newSubTask.getStartTime())) {
                epic.setStartTime(newSubTask.getStartTime());
            }

            if (epic.getEndTime() == null || epic.getEndTime().isBefore(newSubTask.getEndTime())) {
                epic.setEndTime(newSubTask.getEndTime());
            }

            epic.setDuration(epic.getDuration().plus(newSubTask.getDuration()));
        }
        updatePrioritizedTasks();
        return newSubTask;
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        int id = nextId++;
        newEpic.setId(id);
        epics.put(newEpic.getId(), newEpic);
        updatePrioritizedTasks();
        return newEpic;
    }

    @Override
    public void updateTask(Task updatedTask) {
        if (tasks.containsKey(updatedTask.getId())) {
            tasks.put(updatedTask.getId(), updatedTask);
            updatePrioritizedTasks();
        }
    }

    @Override
    public void updateSubTask(SubTask updatedSubTask) {
        if (subTasks.containsKey(updatedSubTask.getId()) && epics.containsKey(updatedSubTask.getEpicId())) {
            subTasks.put(updatedSubTask.getId(), updatedSubTask);
            Epic epic = epics.get(updatedSubTask.getEpicId());
            epic.updateSubtask(updatedSubTask);
            epic.updateEpicStatus();
            updatePrioritizedTasks();
        }
    }

    @Override
    public void updateEpic(Epic updatedEpic) {
        if (epics.containsKey(updatedEpic.getId())) {
            Epic epic = epics.get(updatedEpic.getId());
            epic.setTitle(updatedEpic.getTitle());
            epic.setDescription(updatedEpic.getDescription());
            updatePrioritizedTasks();
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
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
        updatePrioritizedTasks();
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        subTasks.clear();
        epics.clear();
        updatePrioritizedTasks();
    }

    @Override
    public void removeAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        for (Epic epic : epics.values()) {
            epic.subTasksList.clear();
            epic.updateEpicStatus();
        }
        subTasks.clear();
        updatePrioritizedTasks();
    }

    @Override
    public void removeTaskById(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
        updatePrioritizedTasks();
    }

    @Override
    public void removeEpicById(Integer id) {
        if (epics.containsKey(id)) {
            for (SubTask subTask : epics.get(id).getSubTasks()) {
                subTasks.remove(subTask.getId());
                historyManager.remove(subTask.getId());
            }
            epics.get(id).getSubTasks().clear();
            epics.remove(id);
            historyManager.remove(id);
            updatePrioritizedTasks();
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        if (subTasks.containsKey(id)) {
            Epic epic = getEpicById(subTasks.get(id).getEpicId());
            SubTask subTaskToRemove = subTasks.get(id);
            epic.subTasksList.remove(subTaskToRemove);
            epic.updateEpicStatus();
            subTasks.remove(id);
            historyManager.remove(id);
            updatePrioritizedTasks();
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
    public List<Task> getAllTasks() {
        return tasks.values().stream().toList();
    }


    @Override
    public List<SubTask> getAllSubTasks() {
        return subTasks.values().stream().toList();
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics.values().stream().toList();
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

    public List<Task> getPrioritizedTasks() {
        Collection<Task> tasksCollection = tasks.values();
        List<Task> result = new ArrayList<>();
        Collection<Task> nullStartTime = new ArrayList<>();
        Collection<Task> haveStartTime = new ArrayList<>();

        if (tasksCollection != null) {
            if (prioritizedTasks != null && prioritizedTasks.size() == tasksCollection.size()) {
                result = new ArrayList<>(prioritizedTasks);
            } else {
                for (Task task : tasksCollection) {
                    if (task.getStartTime() == null) {
                        result.add(task);
                    } else {
                        haveStartTime.add(task);
                    }
                }
                result = haveStartTime
                        .stream()
                        .sorted(Comparator.comparing(Task::getStartTime))
                        .collect(Collectors.toList());
                prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getId));
                prioritizedTasks.addAll(result);
            }
            result.addAll(nullStartTime);
        }

        return result;
    }


    public boolean isIntersectedTasks(Task task1, Task task2) {
        return ((task1.getStartTime().isBefore(task2.getEndTime()))
                && (task2.getStartTime().isBefore(task1.getEndTime())));
    }

    @Override
    public boolean isIntersectsExistingSubTask(SubTask inputSubTask) {
        List<Task> intersectedTasks = prioritizedTasks.stream()
                .filter((task) -> isIntersectedTasks(inputSubTask, task))
                .toList();
        return !intersectedTasks.isEmpty();
    }

    public boolean isIntersectsExistingTask(Task inputTask) {
        List<Task> intersectedSubTasks = prioritizedTasks.stream()
                .filter((task) -> isIntersectedTasks(inputTask, task))
                .toList();
        return !intersectedSubTasks.isEmpty();
    }

    private void updatePrioritizedTasks() {
        prioritizedTasks.clear();
        for (Task task : tasks.values()) {
            if (task.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(task);
        }

        for (SubTask subTask : subTasks.values()) {
            if (subTask.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(subTask);
        }

        for (Epic epic : epics.values()) {
            if (epic.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(epic);
        }
    }

    public boolean containsId(int id) {
        return (tasks.containsKey(id) || subTasks.containsKey(id) || epics.containsKey(id));
    }
}
