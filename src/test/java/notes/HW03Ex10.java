package notes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HW03Ex10 {

    @Test
    public void testLenght () {
        String name = "test__________15";

        assertTrue(name.length() > 15, "Name must bigger 15");

    }
}
