package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void init() {
        manager = Managers.getDefault();
    }

    @Test
    public void checkThatManagerCanCreateAndGiveSubtaskById() {
        Epic epic = manager.createEpic(new Epic("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        SubTask subTask = manager.createSubTask(new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), epic.getId()));
        Assertions.assertNotNull(manager.getSubTaskById(subTask.getId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveEpicById() {
        Epic epic = manager.createEpic(new Epic("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        Assertions.assertNotNull(manager.getEpicById(epic.getId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveTaskById() {
        Task task = manager.createTask(new Task("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        Assertions.assertNotNull(manager.getTaskById(task.getId()));
    }

    @Test
    public void checkImmutabilityOfEpicWhenCreatedValueChanged() {
        Epic created = manager.createEpic(new Epic("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        String expected = created.getTitle();
        created.setTitle("c");
        String changed = manager.getEpicById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void checkImmutabilityOfEpicWhenSourceChanged() {
        Epic newEpic = new Epic("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Epic created = manager.createEpic(newEpic);
        String expected = created.getTitle();
        created.setTitle("b");
        String changed = manager.getEpicById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void subTaskTitleMustBeDifferentAfterChanging() {
        Epic newEpic = manager.createEpic(new Epic("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        SubTask created = manager.createSubTask(new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), newEpic.getId()));
        String expected = created.getTitle();
        created.setTitle("a2");
        String changed = manager.getSubTaskById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void taskTitleMustBeDifferentAfterChanging() {
        Task created = manager.createTask(new Task("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        String expected = created.getTitle();
        created.setTitle("c");
        String changed = manager.getTaskById(created.getId()).getTitle();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    public void afterRemoveTaskByIdMustBeNull() {
        Task newTask = manager.createTask(new Task("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        Integer expectedId = newTask.getId();
        manager.removeTaskById(expectedId);
        Assertions.assertNull(manager.getTaskById(expectedId));
    }

    @Test
    public void afterRemoveEpicByIdMustBeNull() {
        Epic newEpic = manager.createEpic(new Epic("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        Integer expectedId = newEpic.getId();
        manager.removeEpicById(expectedId);
        Assertions.assertNull(manager.getEpicById(expectedId));
    }

    @Test
    public void afterRemoveSubTaskByIdMustBeNull() {
        Epic newEpic = manager.createEpic(new Epic("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        SubTask created = manager.createSubTask(new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), newEpic.getId()));
        Integer expectedId = created.getId();
        manager.removeSubtaskById(expectedId);
        Assertions.assertNull(manager.getSubTaskById(expectedId));
    }

    @Test
    public void afterRemoveAllAllTasksReturnMustBeNull() {
        Task created1 = manager.createTask(new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        Task created2 = manager.createTask(new Task("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20)));
        Integer numberOfAllTasksBeforeRemove = manager.getAllTasks().size();
        Assertions.assertEquals(2, numberOfAllTasksBeforeRemove);
        manager.removeAllTasks();
        Integer numberOfAllTasksAfterRemove = manager.getAllTasks().size();
        Assertions.assertEquals(0, numberOfAllTasksAfterRemove);
    }

    @Test
    public void afterRemoveAllAllEpicsReturnMustBeNull() {
        Epic created1 = manager.createEpic(new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        Epic created2 = manager.createEpic(new Epic("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20)));
        Integer numberOfAllEpicsBeforeRemove = manager.getAllEpics().size();
        Assertions.assertEquals(2, numberOfAllEpicsBeforeRemove);
        manager.removeAllEpics();
        Integer numberOfAllEpicsAfterRemove = manager.getAllEpics().size();
        Assertions.assertEquals(0, numberOfAllEpicsAfterRemove);
    }

    @Test
    public void afterRemoveAllAllSubTasksReturnMustBeNull() {
        Epic createdEpic = manager.createEpic(new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10)));
        SubTask created1 = manager.createSubTask(new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), createdEpic.getId()));
        SubTask created2 = manager.createSubTask(new SubTask("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20), createdEpic.getId()));
        Integer numberOfAllSubTasksBeforeRemove = manager.getAllSubTasks().size();
        Assertions.assertEquals(2, numberOfAllSubTasksBeforeRemove);
        manager.removeAllSubTasks();
        Integer numberOfAllTasksAfterRemove = manager.getAllSubTasks().size();
        Assertions.assertEquals(0, numberOfAllTasksAfterRemove);
    }

    @Test
    void historyListMustBeNotNullAfterTasksWereAdded() {
        Task newTask = new Task("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        manager.createTask(newTask);
        manager.getTaskById(newTask.getId());
        //historyManager.addTask(newTask);
        final List<Task> history = manager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void checkUpdatedTaskStatus() {
        Task newTask = new Task("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        manager.createTask(newTask);
        Status expected = manager.getTaskById(newTask.getId()).getStatus();
        manager.getTaskById(newTask.getId()).setStatus(Status.DONE);
        manager.updateTask(manager.getTaskById(newTask.getId()));
        Status changed = manager.getTaskById(newTask.getId()).getStatus();
        Assertions.assertNotEquals(expected, changed);
    }

    @Test
    void createdTaskMustBeNotNullAndEqualsByIdAndBeAddedToAlTasks() {
        Task task = new Task("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        final int taskId = manager.createTask(task).getId();
        final Task savedTask = manager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> tasks = manager.getAllTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewTask() {
        assertDoesNotThrow(() -> {
            Task task = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createTask(task);
            int taskId = task.getId();

            final Task savedTask = manager.getTaskById(taskId);

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(task, savedTask, "Задачи не совпадают.");

            final List<Task> tasks = manager.getAllTasks();

            assertNotNull(tasks, "Задачи не возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
        });
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        manager.createEpic(epic);
        int epicId = epic.getId();

        final Epic savedTask = manager.getEpicById(epicId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = manager.getAllEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewSubTask() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);

            SubTask subTask = new SubTask("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());
            manager.createSubTask(subTask);

            final SubTask savedTask = manager.getSubTaskById(subTask.getId());

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(subTask, savedTask, "Задачи не совпадают.");

            final List<SubTask> subTasks = manager.getAllSubTasks();

            assertNotNull(subTasks, "Задачи не возвращаются.");
            assertEquals(1, subTasks.size(), "Неверное количество задач.");
            assertEquals(subTask, subTasks.getFirst(), "Задачи не совпадают.");
        });
    }

    @Test
    void shouldReturnEpicById() {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        manager.createEpic(epic);

        Epic epic2 = manager.getEpicById(epic.getId());

        Assertions.assertEquals(epic, epic2);
    }

    @Test
    void shouldReturnTaskById() {
        assertDoesNotThrow(() -> {
            Task task = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createTask(task);

            Task task2 = manager.getTaskById(task.getId());

            Assertions.assertEquals(task, task2);
        });
    }

    @Test
    void shouldReturnSubTaskById() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);

            SubTask subTask = new SubTask("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());
            manager.createSubTask(subTask);

            SubTask subTask2 = manager.getSubTaskById(subTask.getId());

            Assertions.assertEquals(subTask, subTask2);
        });
    }

    @Test
    void shouldEqualTaskFieldsBeforeAndAfterCreate() {
        assertDoesNotThrow(() -> {
            Task oldTask = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createTask(oldTask);

            Task newTask = manager.getTaskById(oldTask.getId());


            Assertions.assertEquals(oldTask.getTitle(), newTask.getTitle());
            Assertions.assertEquals(oldTask.getDescription(), newTask.getDescription());
            Assertions.assertEquals(oldTask.getStatus(), newTask.getStatus());
            Assertions.assertEquals(oldTask.getClass(), newTask.getClass());
        });
    }

    @Test
    void shouldNotSubtasksIdEqualToEpicId() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);

            SubTask subTask = new SubTask("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());

            List<SubTask> subTasksBeforeUpdate = manager.getAllSubTasks();

            subTask.setId(1);
            manager.updateSubTask(subTask);

            List<SubTask> subTasksAfterUpdate = manager.getAllSubTasks();

            Assertions.assertArrayEquals(subTasksBeforeUpdate.toArray(), subTasksAfterUpdate.toArray());
        });
    }

    @Test
    void shouldNotEpicContainRemovedTaskId() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);

            SubTask subTask = new SubTask("a", "b", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());
            manager.createSubTask(subTask);

            manager.removeSubtaskById(subTask.getId());

            List<SubTask> subTasksList = manager.getAllSubtasksOfEpic(epic);
            Assertions.assertEquals(subTasksList.size(), 0);
        });
    }

    @Test
    void shouldReturnTrueWhenTaskIncludesAnotherTask() {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(40), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        manager.createTask(task1);

        Task task2 = new Task("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 15));
        manager.createTask(task2);
        boolean isIntersected = manager.isIntersectedTasks(task1, task2);

        Assertions.assertTrue(isIntersected);

        isIntersected = manager.isIntersectedTasks(task2, task1);

        Assertions.assertTrue(isIntersected);
    }

    @Test
    void shouldReturnTrueWhenTaskInctersectsAnotherTask() {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(60), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        Task task2 = new Task("a2", "b2", Duration.ofMinutes(60), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 20));

        boolean isIntersected = manager.isIntersectedTasks(task1, task2);

        Assertions.assertTrue(isIntersected);

        isIntersected = manager.isIntersectedTasks(task2, task1);

        Assertions.assertTrue(isIntersected);
    }

    @Test
    void shouldBeFalseWhenTasksRangesAreNotIntersects() {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        Task task2 = new Task("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20));

        boolean isIntersected = manager.isIntersectedTasks(task1, task2);

        Assertions.assertFalse(isIntersected);

        isIntersected = manager.isIntersectedTasks(task2, task1);

        Assertions.assertFalse(isIntersected);
    }

    @Test
    void shouldReturnNewIfAllSubTasksOfEpicAreNew() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);

            SubTask subTask = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());
            manager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                    20, 20, 20), epic.getId());
            manager.createSubTask(subTask2);

            epic = manager.getEpicById(epic.getId());

            assertEquals(epic.getStatus(), Status.NEW);
        });
    }

    @Test
    void shouldReturnDoneIfAllSubTasksOfEpicAreDone() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);

            SubTask subTask = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());
            subTask.setStatus(Status.DONE);
            manager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                    20, 20, 20), epic.getId());
            subTask2.setStatus(Status.DONE);
            manager.createSubTask(subTask2);

            epic = manager.getEpicById(epic.getId());
            assertEquals(epic.getStatus(), Status.DONE);
        });
    }

    @Test
    void shouldReturnInProgressIfSubTasksOfEpicInNewAndDoneStatus() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);
            SubTask subTask = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());
            subTask.setStatus(Status.NEW);
            manager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                    20, 20, 20), epic.getId());
            subTask2.setStatus(Status.DONE);
            manager.createSubTask(subTask2);


            epic = manager.getEpicById(epic.getId());

            assertEquals(epic.getStatus(), Status.IN_PROGRESS);
        });
    }

    @Test
    void shouldReturnInProgressIfSubTasksOfEpicInProgressStatus() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            manager.createEpic(epic);

            SubTask subTask = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10), epic.getId());
            subTask.setStatus(Status.IN_PROGRESS);
            manager.createSubTask(subTask);

            SubTask subTask2 = new SubTask("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                    20, 20, 20), epic.getId());
            subTask2.setStatus(Status.IN_PROGRESS);
            manager.createSubTask(subTask2);

            epic = manager.getEpicById(epic.getId());

            assertEquals(epic.getStatus(), Status.IN_PROGRESS);
        });
    }

    @Test
    void shouldThrowNothingOnNoIntersectedTaskTimes() {
        assertDoesNotThrow(() -> {
            Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10));
            Task task2 = new Task("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                    20, 20, 20));
            manager.createTask(task1);
            manager.createTask(task2);
        });
    }
}