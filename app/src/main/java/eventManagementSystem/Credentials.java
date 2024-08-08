package eventManagementSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidPasswordException;
import exceptions.InvalidUsernameException;

public class Credentials {
    public static final Scanner INPUT = new Scanner(System.in);

    public static void addUsernameAndPassword(String filePath) {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            System.out.println("Sign Up:");
            System.out.print("Enter username: ");
            String username = INPUT.nextLine();

            // Check if the username already exists
            if (isUsernameExists(username, filePath)) {
                throw new InvalidUsernameException("Username already exists. Please choose a different username.");
            }

            // Validate password
            String password;
            do {
                try {
                    System.out.print(
                            "Create a password (at least 6 characters, 1 capital letter, 1 special character): ");
                    password = INPUT.nextLine();
                    validatePassword(password);
                } catch (InvalidPasswordException e) {
                    System.out.println(e.getMessage());
                    continue; // Prompt user for a new password
                }
                break; // Exit the loop if the password is valid

            } while (true);

            // Write the new username-password pair to the file
            writer.write(username + "," + password);
            writer.newLine();
            System.out.println("Account created successfully.");

        } catch (IOException | InvalidUsernameException e) {
            e.printStackTrace();
        }
    }

    private static void validatePassword(String password) throws InvalidPasswordException {
        // Check if the password is at least 6 characters long
        if (password.length() < 6) {
            throw new InvalidPasswordException("Password must be at least 6 characters long.");
        }

        // Check if the password contains at least 1 capital letter
        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("Password must contain at least 1 capital letter.");
        }

        // Check if the password contains at least 1 special character
        Pattern specialCharacterPattern = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
        Matcher matcher = specialCharacterPattern.matcher(password);
        if (!matcher.find()) {
            throw new InvalidPasswordException("Password must contain at least 1 special character.");
        }
    }

    public static int isValidCredentials(String username, String password, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return lineNum; // Username and password match
                }
                lineNum++;
            }
        }
        return 0; // Invalid username or password
    }

    private static boolean isUsernameExists(String username, String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    return true; // Username already exists
                }
            }
        }
        return false; // Username does not exist
    }

}
