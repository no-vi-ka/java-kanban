package tasks;

import tasks.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TaskTest {
    @Test
    public void checkTasksEqualWhenIdsEqual() {
        Task task1 = new Task("a1", "b1");
        Task task2 = new Task("a2", "b2");
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }
}