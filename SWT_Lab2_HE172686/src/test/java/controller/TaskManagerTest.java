package controller;

import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import static controller.TaskManager.*;
import static controller.TaskManager.addTask;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private final InputStream originalSystemIn = System.in;
    private ByteArrayInputStream testIn;
//    private final PrintStream originalSystemOut = System.out;
//    private ByteArrayOutputStream testOut;

    private static TaskManager taskManager ;


    @BeforeEach
    void setUp() {
//     System.setIn(new ByteArrayInputStream("".getBytes()));
//    taskManager = new TaskManager(System.in);
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalSystemIn);
        taskManager = null;
    }




    //=======================CHECK INT LIMIT============================================(CKL) 9
    @ParameterizedTest
    @CsvSource({
            "5, 5",
            "11, -1",
            "-2, -1"
    })
    void testValidInputWithinRangeParameterizedCKL01(int input, int expected) {
//        TaskManager taskManager = new TaskManager(new ByteArrayInputStream((input + "\n").getBytes()));
        testIn = new ByteArrayInputStream((input + "\n").getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        int result = checkIntLimit(1, 10);
        assertEquals(expected, result);
    }  //5

    @Test
    void testInputSpecCKL02() {
        testIn = new ByteArrayInputStream("@abc\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkIntLimit(1, 6);
        assertEquals(-1, result);
    } //


    @Test
    void testValidInputCKL05() {
        testIn = new ByteArrayInputStream("   \n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkIntLimit(1, 6);
        assertEquals(-1, result);
    }

    //==========================check Input Date==========================================(DC)
    @Test
    void testCheckInputDateValidDC01() {
        testIn = new ByteArrayInputStream("15-13-2003\n32-12-2000\n31-12-2003\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String date = checkInputDate();
        assertEquals("31-12-2003", date);
    }

    @Test
    void testCheckInputDateLunarYearDC02() {
        testIn = new ByteArrayInputStream("29-02-2001\n15--2-2001\n29-02-2000\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String validdate = checkInputDate();

        assertEquals("29-02-2000", validdate);
    }

    //==========================CHECK INPUT STRING==========================================(IPST)

    @Test
    void testCheckInputStringNonEmptyIPST01() {
        testIn = new ByteArrayInputStream("Hello, World!\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String result = checkInputString();
        assertEquals("Hello, World!", result);
    }

    @Test
    void testCheckInputStringEmptyThenNonEmptyIPST02() {
        testIn = new ByteArrayInputStream("\nHello, World!\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String result = checkInputString();
        assertEquals("Hello, World!", result);
    }

    //==========================CHECK INPUT INT(IPI)
    @Test
    void testCheckInputIntValidIPST03IPI01() {
        testIn = new ByteArrayInputStream("123\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkInputInt();
        assertEquals(123, result);
    }

    @Test
    void testCheckInputIntInvalidThenValidIPPI02() {
        testIn = new ByteArrayInputStream("333@\n O\n222\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkInputInt();
        assertEquals(222, result);
    }

    //==========================CHECK INPUT TASK TYPEID=========================================(IPTTID)
    @ParameterizedTest
    @CsvSource({
            "1, code",   // valid
            "2, test",  // valid
            "4, learn",  // valid
            "5, ",   // invalid
            "-1, ",  // invalid
    })
    void testValidInputWithinRangeNumberIPTTID01(int input, String expected) {
        testIn = new ByteArrayInputStream((input + "\n").getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputTaskTypeId();
        if (input == 5 || input == -1) {
            assertNull(result);
        } else {
            assertEquals(expected, result);
        }

    }

    @Test
    void testInValidInputWithLetterAndDigitIPTTID02() {
        testIn = new ByteArrayInputStream("1a\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputTaskTypeId();
        assertNull(result);
    }

    @Test
    void testInValidInputWithEmptyIPTTID03() {
        testIn = new ByteArrayInputStream(" \n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputTaskTypeId();
        assertNull(result);
    }

    //======================================Check Input Plant===============================================(IPPL)
    // begin 8.0 step +,5 to 17.5
    @Test
    void testCheckInputPlan_Valid_IPPL01() {
        testIn = new ByteArrayInputStream("9.5\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputPlan();
        assertEquals("9.5", result);
    }

    @Test
    void testCheckInputPlan_InvalidThenValid_IPPL02() {
        testIn = new ByteArrayInputStream("7.5\n8.2\n18.0\n10.0\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputPlan();
        assertEquals("10.0", result);
    }

    //======================================ADD TASK================================================(ADT)

    @ParameterizedTest
    @CsvSource({
            // Valid cases
            "1,  Aname, 2, 19-12-2003, 8.0, 10.0, John Doe, Jane Doe, true",
            "2,  Bname, 1, 20-12-2003, 9.0, 11.0, Alice, Bob, true",
            // Invalid cases
            "4,  Dname, 4, 19-12-2003, 7.0, 10.0, Eve, Frank, false", // Invalid planFrom (less than 8.0)
            "5,  , 5, 19-12-2003, 8.0, 13.0, Grace, Hank, false", // Name null (greater than 17.5)
            "6,  Gname, , 19-12-2003, 8.0, 10.0, Grace, Hank, false", // Invalid type
            "6,  FAname, , 33-12-2003, 8.0, 10.0, Grace, Hank, false", // Invalid type

    })
    void testAddTaskADT01(int id, String requirementName, String taskTypeId, String date, String planFrom, String planTo, String assignee, String reviewer, boolean shouldBeAdded) {
        String input = String.format("%s\n%s\n%s\n%s\n%s\n%s\n%s\n",
                requirementName, taskTypeId, date, planFrom, planTo, assignee, reviewer);
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        ArrayList<Task> taskList = new ArrayList<>();

        try {
            addTask(taskList, id);
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//            if (requirementName == null || requirementName.isEmpty()) {
//                throw new IllegalArgumentException("Requirement name is null or empty");
//            }
            if (taskTypeId == null || taskTypeId.isEmpty()) {
                throw new IllegalArgumentException("Task type ID is null or empty");

            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format: " + date);
            assertEquals(0, taskList.size());
            return; // Exit the test method
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            assertEquals(0, taskList.size());
            return; // Exit the test method
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            assertEquals(0, taskList.size());
            return; // Exit the test method
        }

        String taskTypeName;
        switch (taskTypeId) {
            case "1":
                taskTypeName = "code";
                break;
            case "2":
                taskTypeName = "test";
                break;
            case "3":
                taskTypeName = "manager";
                break;
            case "4":
                taskTypeName = "learn";
                break;
            default:
                taskTypeName = null;
        }

        double valuePlanTo = Double.parseDouble(planTo);
        double valuePlanFrom = Double.parseDouble(planFrom);

        if (shouldBeAdded && valuePlanTo > valuePlanFrom) {
            assertEquals(1, taskList.size());
            Task addedTask = taskList.get(0);
            assertNotNull(addedTask);
            assertEquals(id, addedTask.getId());
            assertEquals(taskTypeName, addedTask.getTaskTypeId());
            assertEquals(requirementName, addedTask.getRequirementName());
            assertEquals(date, addedTask.getDate());
            assertEquals(planFrom, addedTask.getPlanFrom());
            assertEquals(planTo, addedTask.getPlanTo());
            assertEquals(assignee, addedTask.getassign());
            assertEquals(reviewer, addedTask.getreviewer());
        } else {
            assertEquals(0, taskList.size());
        }
    }





}






