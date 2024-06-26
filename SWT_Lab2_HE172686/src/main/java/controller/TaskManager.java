package controller;

import model.*;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author l3ing
 */
public class TaskManager {

    public static  Scanner in = new Scanner(System.in);
    public static final String PLAN_VALID = "^[0-9]{1,2}\\.5|[0-9]{1,2}\\.0$";

    public TaskManager(InputStream input) {
        this.in = new Scanner(input);
    }

    public static int checkIntLimit(int min, int max) {
        while (true) {
            try {
                int n = Integer.parseInt(in.nextLine());
                if (n < min || n > max) {
//                    throw new NumberFormatException();
                    return -1;
                }
                return n;
            } catch (Exception ex) {
                return -1;
            }
        }
    }

    public static String checkInputDate() {
        while (true) {
            try {
                String result = in.nextLine().trim();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                Date date = format.parse(result);
                if (result.equalsIgnoreCase(format.format(date))) {
                    return result;
                } else {
                    System.err.println("Re-input");
                    return "Re-input";

                }
            } catch (Exception ex) {
                System.err.println("Re-input");
                return "Re-input";
            }
        }
    }

    public static String checkInputString() {
        while (true) {
            String result = in.nextLine().trim();
            if (result.length() == 0) {
                System.err.println("Not empty.");
            } else {
                return result;
            }
        }
    }

    public static int checkInputInt() {
        while (true) {
            try {
                int result = Integer.parseInt(in.nextLine());
                return result;
            } catch (NumberFormatException ex) {
                System.err.println("Re-input");
            }
        }
    }

    public static String checkInputTaskTypeId() {
        while (true) {
            int n = checkIntLimit(1, 4);
            String result = null;
            switch (n) {
                case 1:
                    result = "code";
                    break;
                case 2:
                    result = "test";
                    break;
                case 3:
                    result = "manager";
                    break;
                case 4:
                    result = "learn";
            }
            return result;
        }
    }

    public static String checkInputPlan() {
        while (true) {
            String result = checkInputString();
            if (result.matches(PLAN_VALID) && Double.parseDouble(result) >= 8.0
                    && Double.parseDouble(result) <= 17.5) {
                return result;
            } else {
                System.err.println("Re-input.");
            }
        }
    }

    public static void addTask(ArrayList<Task> lt, int id) {
        System.out.print("Enter Requirement Name: ");
        String requirementName = checkInputString();
        System.out.print("Enter Task Type: ");
        String taskTypeId = checkInputTaskTypeId();
        System.out.print("Enter Date: ");
        String date = checkInputDate();
        System.out.print("Enter From: ");
        String planFrom = checkInputPlan();
        System.out.print("Enter To: ");
        String planTo = checkInputPlan();
        double valuePlanTo= Double.parseDouble(planTo);
        double valuePlanFrom= Double.parseDouble(planFrom);

        while (valuePlanTo - valuePlanFrom <0){
            System.out.println("Plan to great than plan form");
            System.out.print("Enter From: ");
             planFrom = checkInputPlan();
            System.out.print("Enter To: ");
             planTo = checkInputPlan();
            valuePlanTo= Double.parseDouble(planTo);
            valuePlanFrom= Double.parseDouble(planFrom);
        }
        System.out.print("Enter Assignee: ");
        String assign = checkInputString();
        System.out.print("Enter Reviewer: ");
        String reviewer = checkInputString();
        lt.add(new Task(id, taskTypeId, requirementName, date, planFrom, planTo, assign, reviewer));
        System.out.println("Add Task Success.");
    }

    public static void deleteTask(ArrayList<Task> lt, int id) {
        if (lt.isEmpty()) {
            System.err.println("List empty");
            return;
        }
        int findId = findTaskExist(lt);
        if (findId != -1) {
            lt.remove(findId);
            for (int i = findId; i < lt.size(); i++) {
                lt.get(i).setId(lt.get(i).getId() - 1);
            }
            System.err.println("Delete success.");
        }
    }

    public static int findTaskExist(ArrayList<Task> lt) {
        System.out.print("Enter id: ");
        int id = checkInputInt();
        for (int i = 0; i < lt.size(); i++) {
            if (lt.get(i).getId() == id) {
                return i;
            }
        }
        System.err.println("Not found.");
        return -1;
    }

    public static void print(ArrayList<Task> lt) {
        if (lt.isEmpty()) {
            System.err.println("List empty.");
            return;
        }
        System.out.printf("%-5s%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "ID", "Name", "Task Type", "Date", "Time", "Assign", "Reviewer");
        for (int i = 0; i < lt.size(); i++) {
            System.out.printf("%-5d%-15s%-15s%-15s%-15.1f%-15s%-15s\n",
                    lt.get(i).getId(),
                    lt.get(i).getRequirementName(),
                    lt.get(i).getTaskTypeId(),
                    lt.get(i).getDate(),
                    Double.parseDouble(lt.get(i).getPlanTo()) - Double.parseDouble(lt.get(i).getPlanFrom()),
                    lt.get(i).getassign(),
                    lt.get(i).getreviewer()
            );

        }
    }

    public static void display() {
        ArrayList<Task> lt = new ArrayList<>();
        int choice;
        int id = 1;
        while (true) {
            System.out.println("1. Add Task");
            System.out.println("2. Delete Task");
            System.out.println("3. Display Task");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = checkIntLimit(1, 4);
            switch (choice) {
                case 1:
                    addTask(lt, id);
                    id++;
                    break;
                case 2:
                    deleteTask(lt, id);
                    id--;
                    break;
                case 3:
                    print(lt);
                    break;
                case 4:
                    return;

            }
        }
    }


}
