package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void init() {
        manager = Managers.getDefault();
    }

    @Test
    public void checkThatManagerCanCreateAndGiveSubtaskById() {
        Epic epic = manager.createEpic(new Epic("a", "b"));
        SubTask subTask = manager.createSubTask(new SubTask("a1", "b1", epic.getId()));
        Assertions.assertNotNull(manager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveEpicById() {
        Epic epic = manager.createEpic(new Epic("", ""));
        Assertions.assertNotNull(manager.getEpicById(epic.getId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveTaskById() {
        Task task = manager.createTask(new Task("", ""));
        Assertions.assertNotNull(manager.getTaskById(task.getId()));
    }

    @Test
    public void checkImmutabilityOfEpicWhenCreatedValueChanged() {
        Epic created = manager.createEpic(new Epic("a", "b"));
        String expected = created.getTitle();
        created.setTitle("c");
        String changed = manager.getEpicById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void checkImmutabilityOfEpicWhenSourceChanged() {
        Epic newEpic = new Epic("a", "b");
        Epic created = manager.createEpic(newEpic);
        String expected = created.getTitle();
        created.setTitle("b");
        String changed = manager.getEpicById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void subTaskTitleMustBeDifferentAfterChanging() {
        Epic newEpic = manager.createEpic(new Epic("a", "b"));
        SubTask created = manager.createSubTask(new SubTask("a1", "b1", newEpic.getId()));
        String expected = created.getTitle();
        created.setTitle("a2");
        String changed = manager.getSubTaskById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void taskTitleMustBeDifferentAfterChanging() {
        Task created = manager.createTask(new Task("a", "b"));
        String expected = created.getTitle();
        created.setTitle("c");
        String changed = manager.getTaskById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void afterRemoveTaskByIdMustBeNull() {
        Task newTask = manager.createTask(new Task("a", "b"));
        Integer expectedId = newTask.getId();
        manager.removeTaskById(expectedId);
        Assertions.assertNull(manager.getTaskById(expectedId));
    }

    @Test
    public void afterRemoveEpicByIdMustBeNull() {
        Epic newEpic = manager.createEpic(new Epic("a", "b"));
        SubTask created = manager.createSubTask(new SubTask("a1", "b1", newEpic.getId()));
        Integer expectedId = newEpic.getId();
        manager.removeEpicById(expectedId);
        Assertions.assertNull(manager.getEpicById(expectedId));
    }

    @Test
    public void afterRemoveSubTaskByIdMustBeNull() {
        Epic newEpic = manager.createEpic(new Epic("a", "b"));
        SubTask created = manager.createSubTask(new SubTask("a1", "b1", newEpic.getId()));
        Integer expectedId = created.getId();
        manager.removeSubtaskById(expectedId);
        Assertions.assertNull(manager.getSubTaskById(expectedId));
    }

    @Test
    public void afterRemoveAllAllTasksReturnMustBeNull() {
        Task created1 = manager.createTask(new Task("a1", "b1"));
        Task created2 = manager.createTask(new Task("a2", "b2"));
        manager.removeAllTasks();
        Integer numberOfAllTasksAfterRemove = manager.getAllTasks().size();
        Assertions.assertEquals(0, numberOfAllTasksAfterRemove);
    }

    @Test
    public void afterRemoveAllAllEpicsReturnMustBeNull() {
        Epic created1 = manager.createEpic(new Epic("a1", "b1"));
        Epic created2 = manager.createEpic(new Epic("a2", "b2"));
        manager.removeAllEpics();
        Integer numberOfAllEpicsAfterRemove = manager.getAllEpics().size();
        Assertions.assertEquals(0, numberOfAllEpicsAfterRemove);
    }

    @Test
    public void afterRemoveAllAllSubTasksReturnMustBeNull() {
        Epic createdEpic = manager.createEpic(new Epic("a1", "b1"));
        SubTask created1 = manager.createSubTask(new SubTask("a1", "b1", createdEpic.getId()));
        SubTask created2 = manager.createSubTask(new SubTask("a2", "b2", createdEpic.getId()));
        manager.removeAllSubTasks();
        Integer numberOfAllTasksAfterRemove = manager.getAllSubTasks().size();
        Assertions.assertEquals(0, numberOfAllTasksAfterRemove);
    }

    @Test
    void historyListMustBeNotNullAfterTasksWereAdded() {
        Task newTask = new Task("a", "b");
        manager.createTask(newTask);
        manager.getTaskById(newTask.getId());
        //historyManager.addTask(newTask);
        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void checkUpdatedTaskStatus() {
        Task newTask = new Task("a", "b");
        manager.createTask(newTask);
        Status expected = manager.getTaskById(newTask.getId()).getStatus();
        manager.getTaskById(newTask.getId()).setStatus(Status.DONE);
        manager.updateTask(manager.getTaskById(newTask.getId()));
        Status changed = manager.getTaskById(newTask.getId()).getStatus();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void updatedSubTaskMustBeDifferWithFirstVersion() {
        Epic createdEpic = manager.createEpic(new Epic("a1", "b1"));
        SubTask created = manager.createSubTask(new SubTask("a1", "b1", createdEpic.getId()));
        SubTask expected = manager.getSubTaskById(created.getId());
        manager.getSubTaskById(created.getId()).setDescription("c1");
        manager.updateSubTask(expected);
        SubTask changed = manager.getSubTaskById(created.getId());
        Assertions.assertNotEquals(manager.getHistory().getFirst(), changed);
    }

    @Test
    public void updatedEpicMustBeDifferWithFirstVersion() {
        Epic createdEpic = manager.createEpic(new Epic("a1", "b1"));
        SubTask createdSubTask1 = manager.createSubTask(new SubTask("a1", "b1", createdEpic.getId()));
        Epic expected = manager.getEpicById(createdEpic.getId());
        SubTask createdSubTask2 = manager.createSubTask(new SubTask("a2", "b2", createdEpic.getId()));
        manager.getEpicById(createdEpic.getId());
        manager.updateEpic(expected);
        Epic changed = manager.getEpicById(createdEpic.getId());
        Assertions.assertNotEquals(manager.getHistory().getFirst(), changed);
    }

    @Test
    void createdTaskMustBeNotNullAndEqualsByIdAndBeAddedToAlTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = manager.createTask(task).getId();
        final Task savedTask = manager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }
}