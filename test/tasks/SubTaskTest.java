package tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SubTaskTest {
    @Test
    public void checkSubtasksEqualWhenIdsEqual() {
        SubTask subtask1 = new SubTask("a1", "b1", 0);
        SubTask subtask2 = new SubTask("a2", "b2", 0);
        subtask1.setId(1);
        subtask2.setId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }
}