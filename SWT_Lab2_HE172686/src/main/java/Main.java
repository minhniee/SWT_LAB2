import controller.TaskManager;
import controller.UserProgram;

import static controller.UserProgram.checkIntLimit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
            while (true) {
                System.out.println("1. User Program");
                System.out.println("2. Task manager");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = checkIntLimit(1, 3);
                switch (choice) {
                    case 1:
//                        addAccount(la);
                        UserProgram.display();
                        break;
                    case 2:
                        TaskManager.display();
//                        login(la);

                        break;
                    case 3:
                        return;
                }
            }
        }

    }
