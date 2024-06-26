package controller;

import model.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
/**
 *
 * @author l3ing
 */
public class UserProgram {

    private static final Scanner in = new Scanner(System.in);
    private static final String PHONE_VALID = "^\\d{9,10}$";
    private static final String EMAIL_VALID = "^[0-9A-Za-z+_.%]+@[0-9A-Za-z.-]+\\.[A-Za-z]{2,4}$";

        public static int checkIntLimit(int min, int max) {
            while (true) {
                try {
                    int n = Integer.parseInt(in.nextLine());
                    if (n < min || n > max) {
                        throw new NumberFormatException();
                    }
                    return n;
                } catch (NumberFormatException ex) {
                    System.err.println("Re-input");
                }
            }
        }

    private static String checkInputDate() {
        while (true) {
            try {
                String result = in.nextLine().trim();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date date = format.parse(result);
                if (result.equalsIgnoreCase(format.format(date))) {
                    return result;
                } else {
                    System.err.println("Re-input");
                }
            } catch (NumberFormatException | ParseException ex) {
                System.err.println("Re-input");
            }
        }
    }

    private static String checkInputPhone() {
        while (true) {
            String result = in.nextLine().trim();
            if (!result.isEmpty() && result.matches(PHONE_VALID)) {
                return result;
            } else {
                System.err.println("Re-input");
            }
        }
    }

    private static String checkInputEmail() {
        while (true) {
            String result = in.nextLine().trim();
            if (!result.isEmpty() && result.matches(EMAIL_VALID)) {
                return result;
            } else {
                System.err.println("Re-input");
            }
        }
    }

    private static String checkInputString() {
        while (true) {
            String result = in.nextLine().trim();
            if (result.isEmpty()) {
                System.err.println("Not empty");
            } else {
                return result;
            }
        }
    }

    private static String checkInputUsername(ArrayList<Account> la) {
        while (true) {
            String result = checkInputString();
            for (Account account : la) {
                if (result.equalsIgnoreCase(account.getUsername())) {
                    System.err.println("Username exist!!!");
                }
            }
            return result;
        }
    }


    public static String MD5Encryption(String input) {
        try {
            // Create an MD5 message digest instance
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Update the message digest with the input string's bytes
            md.update(input.getBytes());

            // Complete the hash computation
            byte[] digest = md.digest();

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addAccount(ArrayList<Account> la) {
        System.out.print("Enter Username: ");
        String username = checkInputUsername(la);
        System.out.print("Enter Password: ");
        String password = checkInputString();
        System.out.print("Enter Name: ");
        String name = checkInputString();
        System.out.print("Enter Phone: ");
        String phone = checkInputPhone();
        System.out.print("Enter email: ");
        String email = checkInputEmail();
        System.out.print("Enter address: ");
        String address = checkInputString();
        System.out.print("Enter Date Of Birth: ");
        String dateOfBirth = checkInputDate();
        la.add(new Account(username, MD5Encryption(password), name, phone, email,
                address, dateOfBirth));
        System.out.println("Add success!!!");
    }

    private static void login(ArrayList<Account> la) {
        if (la.isEmpty()) {
            System.err.println("Accout empty.");
            return;
        }
        System.out.print("Enter username: ");
        String username = checkInputString();
        System.out.print("Enter Password: ");
        String password = checkInputString();
        Account accoutLogin = findAccount(la, username, password);
        if (accoutLogin != null) {
            System.out.println("Wellcome");
            System.out.print("Hi " + accoutLogin.getUsername()
                    + ", do you want chage password now? Y/N: ");
            changePassword(accoutLogin);
        } else {
            System.err.println("Invalid username or password.");
        }
    }

    private static Account findAccount(ArrayList<Account> la, String username,
            String password) {
        for (Account account : la) {
            if (username.equalsIgnoreCase(account.getUsername())) {
                if (MD5Encryption(password).equalsIgnoreCase(account.getPassword())) {
                    return account;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private static void changePassword(Account accoutLogin) {
        String choice;
        while (true) {
            choice = in.nextLine().trim();
            if (choice.isEmpty()) {
                System.err.println("Not empty!!!");
            } else if (choice.equalsIgnoreCase("Y")
                    || choice.equalsIgnoreCase("N")) {
                break;
            } else {
                System.err.println("Re-input");
            }
        }
        if (choice.equalsIgnoreCase("Y")) {
            System.out.print("Old password: ");
            String oldPassword = checkInputString();
            System.out.print("New password: ");
            String newPassword = checkInputString();
            System.out.print("Renew password: ");
            String renewPassword = checkInputString();
            if (!MD5Encryption(oldPassword).equalsIgnoreCase(accoutLogin.getPassword())) {
                System.err.println("Old password incorrect.");
            }
            if (!renewPassword.equalsIgnoreCase(newPassword)) {
                System.err.println("New password and Renew password not the same.");
            }
            if (MD5Encryption(oldPassword).equalsIgnoreCase(accoutLogin.getPassword())
                    && newPassword.equalsIgnoreCase(renewPassword)) {
                accoutLogin.setPassword(MD5Encryption(newPassword));
                System.out.println("Change password success");
            }
        }
    }

    public static void display() {
        ArrayList<Account> la = new ArrayList<>();
        while (true) {
            System.out.println("1. Add user");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = checkIntLimit(1, 3);
            switch (choice) {
                case 1:
                    addAccount(la);
                    break;
                case 2:
                    login(la);
                    break;
                case 3:
                    return;
            }
        }
    }


}
