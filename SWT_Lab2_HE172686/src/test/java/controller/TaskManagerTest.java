package controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.NoSuchElementException;

import static controller.TaskManager.checkIntLimit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private final InputStream originalIn = System.in;

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        System.setIn( new ByteArrayInputStream("".getBytes()));
        taskManager = new TaskManager(System.in);
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        taskManager = null;
    }

    @ParameterizedTest
    @CsvSource({
            "5, 5",
            "7, 7",
            "1, 1",
            "11, -1",
            "-2, -1"
    })
    void testValidInputWithinRangeParameterizedCKL01(int input, int expected) {
        TaskManager taskManager = new TaskManager(new ByteArrayInputStream((input + "\n").getBytes()));
        int result = taskManager.checkIntLimit(1, 10);
        assertEquals(expected, result);
    }

    @Test
    void testInputSpecCKL02() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("@abc\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkIntLimit(1, 6);
        assertEquals(-1, result);
    }
    @Test
    void testInputLetterCKL03() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("abc\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkIntLimit(1, 6);
        assertEquals(-1, result);
    }
    @Test
    void testInputLetterDigitsCKL04() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("123abc\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkIntLimit(1, 6);
        assertEquals(-1, result);
    }
@Test
    void testValidInputCKL05() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("   \n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkIntLimit(1, 6);
        assertEquals(-1, result);
    }



}
