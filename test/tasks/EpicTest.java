package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EpicTest {
    @Test
    public void checkEpicsEqualWhenIdsEqual() {
        Epic epic1 = new Epic("a1", "b1");
        Epic epic2 = new Epic("a2", "b2");
        epic1.setId(1);
        epic2.setId(1);
        Assertions.assertEquals(epic1, epic2);
    }
}