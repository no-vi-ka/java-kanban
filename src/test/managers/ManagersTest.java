package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ManagersTest {

    @Test
    public void checkThatGetDefaultNotNull() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
    public void checkThatGetDefaultHistoryNotNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}