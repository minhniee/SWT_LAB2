package controller;

import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import static controller.TaskManager.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private final InputStream originalIn = System.in;

    private TaskManager taskManager = new TaskManager(System.in);

    @BeforeEach
    void setUp() {
        System.setIn(new ByteArrayInputStream("".getBytes()));
        taskManager = new TaskManager(System.in);
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        taskManager = null;
    }

    //=======================CHECK INT LIMIT============================================(CKL)
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
        int result = checkIntLimit(1, 10);
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

    //==========================check Input Date==========================================(DC)
    @Test
    void testCheckInputDateValidDC01() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("15-12-2003\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String date = checkInputDate();
        assertEquals("15-12-2003", date);
    }

    @Test
    void testCheckInputDateLunarYearDC02() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("29-02-2000\n29-02-2001\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String validdate = checkInputDate();
        assertEquals("29-02-2000", validdate);
        String invalidData = checkInputDate();
        assertEquals("Re-input", invalidData);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "29-02-2000", // lunar year
            "29-02-2001",// not lunar year
            "31-12-2003", // normal date
            "32-12-2000", // Dec have 31 days
            "15-13-2000" // wrong month
    })
    void testCheckInputDateTypesCKL03(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream((data + "\n").getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String date = checkInputDate();
        if (data.equals("29-02-2000") || data.equals("31-12-2003")) {
            assertEquals(date, data);
        } else {
            assertEquals("Re-input", date);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234",
            "asdwd",
            "aa-bb-dddd",
            "21-12-aaa",
            "12-12-12"})
    void testCheckInputDateSpecialCharCKL04(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream((data + "\n").getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String date = checkInputDate();
        assertEquals("Re-input", date);
    }

    //==========================CHECK INPUT STRING==========================================(IPST)

    @Test
    void testCheckInputStringNonEmptyIPST01() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("Hello, World!\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String result = checkInputString();
        assertTrue(
                result.equals("Hello, World!"));
    }

    @Test
    void testCheckInputStringEmptyThenNonEmptyIPST02() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("\nHello, World!\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        String result = checkInputString();
        assertEquals("Hello, World!", result);
    }

    //==========================CHECK INPUT INT(IPI)
    @Test
    void testCheckInputIntValidIPST03IPI01() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("123\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);

        int result = checkInputInt();
        assertEquals(123, result);
    }

    @Test
    void testCheckInputIntInvalidThenValidIPPI02() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("333@\na423\n222\n".getBytes());
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
            "3, manager",  // valid
            "4, learn",  // valid
            "5, ",   // invalid
            "-1, ",  // invalid
    })
    void testValidInputWithinRangeNumberIPTTID01(int input, String expected) {
        ByteArrayInputStream testIn = new ByteArrayInputStream((input + "\n").getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputTaskTypeId();
        if( expected  =="5" || expected == "-1"){
        assertNull(result);
        }else assertEquals(expected, result);
    }
    @Test
    void testInValidInputWithLetterAndDigitIPTTID02() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("1a\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputTaskTypeId();
        assertNull(result);
    } // asertnull
    @Test
    void testInValidInputWithEmptyIPTTID03() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputTaskTypeId();
        assertNull( result);
    }
    //==========================CHECK INPUT PLAN=========================================(IPPL)
    // begin 8.0 step +,5 to 17.5
    @Test
    void testCheckInputPlan_Valid_IPPL01() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("9.0\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputPlan();
        assertEquals("9.0", result);
    }

    @Test
    void testCheckInputPlan_InvalidThenValid_IPPL02() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("7.0\n7.5\n9\n8.2\n18.0\n10.0\n".getBytes());
        System.setIn(testIn);
        taskManager = new TaskManager(System.in);
        String result = checkInputPlan();
        assertEquals("10.0", result);
    }

    //======================================ADD TASK================================================(ADT)

//     case 1:
//    "code";
//      break;
//     case 2:
//     "test";
//     break;
//     case 3:
//    "manager";
//    break;
//    case 4:
//    "learn";
//==========================ADD TASK=========================================(IPTTID)
    @ParameterizedTest
    @CsvSource({
            // Valid cases
            "1, Requirement A, 2, 19-12-2003, 8.0, 10.0, John Doe, Jane Doe, true",
            "2, Requirement B, 1, 20-12-2003, 9.0, 11.0, Alice, Bob, true",
            // Invalid cases
            "3, Requirement C, 3, 32-12-2003, 8.0, 10.0, Charlie, Dave, false", // Invalid date
            "4, Requirement D, 4, 19-12-2003, 7.0, 10.0, Eve, Frank, false", // Invalid planFrom (less than 8.0)
            "5, Requirement E, 5, 19-12-2003, 8.0, 18.0, Grace, Hank, false", // Invalid planTo (greater than 17.5)
            "6, , 6, 19-12-2003, 8.0, 10.0, Ian, Jack, false", // Empty requirementName
            "7, Requirement F, , 19-12-2003, 8.0, 10.0, Ken, Larry, false" ,// Empty taskTypeId
            "8, Requirement G,2 , 19-12-2003, 15.0, 10.0, Ken, Larry, false" // time form > time to

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
            // Check if the date is valid
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            // check requirementName is null
            if (requirementName == null){
                throw new Exception("requirementName is null");
            }
            // check tasktype is null
            if (taskTypeId == null) {
                throw new Exception("taskTypeId is null");
            }

        } catch (DateTimeParseException e) {
            // Handle invalid date format
            System.out.println("Invalid date format: " + date);
            return; // Exit the test method
        } catch (Exception e) {
            // Handle any other exceptions thrown by invalid inputs
            System.out.println(e.getMessage());
            System.out.println("Something went wrong: " + e.getMessage());
            return; // Exit the test method
        }

        String taskTypeName = "";

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


        if (shouldBeAdded && (valuePlanTo > valuePlanFrom) ) {
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
        } else assertEquals(0, taskList.size());
        taskList.clear();
    }



}





