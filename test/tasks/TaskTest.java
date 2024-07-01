package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class TaskTest {
    @Test
    public void checkTasksEqualWhenIdsEqual() {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Task task2 = new Task("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20));
        task1.setId(1);
        task2.setId(1);
        Assertions.assertEquals(task1, task2);
    }
}