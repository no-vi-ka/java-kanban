package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class EpicTest {
    @Test
    public void checkEpicsEqualWhenIdsEqual() {
        Epic epic1 = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Epic epic2 = new Epic("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20));
        epic1.setId(1);
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }
}