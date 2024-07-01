package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class SubTaskTest {
    @Test
    public void checkSubtasksEqualWhenIdsEqual() {
        SubTask subtask1 = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), 0);
        SubTask subtask2 = new SubTask("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20), 0);
        subtask1.setId(1);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }
}