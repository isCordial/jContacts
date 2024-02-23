import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.nio.file.*;

public class ContactManager {
    // Define constants for file, file path, and scanner (user input).
    private static final String FILE_PATH = "phoneNums.csv";
    private static final Scanner scanner = new Scanner(System.in);
  
    // Main function
    public static void main(String[] args) {
        while (true) {
            System.out.println("[S]earch for existing contact, or [C]reate a new contact? (S/C)");
            String input = scanner.nextLine().trim().toUpperCase();
            
            // switch case, as opposed to if statements.
            // for picking createContact() or searchContact()
            switch (input) {
                case "C":
                    createContact();
                    return;
                case "S":
                    searchContact();
                    return;
                default:
                    System.out.println("INVALID INPUT. Please enter [S] or [C].");
            }
        }
    }

    // Creates a new contact using input from user.
    private static void createContact() {
        String name = promptForName();
        String number = promptForNumber();
        addContact(name, number);
        System.out.println("Success! Contact added.");
    }

    // Prompts user for name.
    private static String promptForName() {
        while (true) {
            System.out.println("Enter the name of the new contact:");
            String name = scanner.nextLine().trim();
            // Sanitize input. Ensure valid alphabetical chars.
            if (name.matches("[A-Za-z ]+") && name.length() <= 60) {
                return name;
            } else {
                System.out.println("Invalid name. Please use alphabetical characters only. Ensure it does not exceed 60 characters.");
            }
        }
    }

    // Prompts user for phone number
    private static String promptForNumber() {
        while (true) {
            System.out.println("Enter the phone number of the new contact:");
            String number = scanner.nextLine().trim();
            // Sanitize input. Ensure valid number and symbolic chars.
            if (number.matches("[0-9-]+") && number.length() <= 25) {
                return number;
            } else {
                System.out.println("Invalid number. Please use numeric characters and dashes only. Ensure it does not exceed 25 characters.");
            }
        }
    }

    // Add contact to file
    private static void addContact(String name, String number) {
      // BufferedWriter not entirely necessary here. But hey, it's efficient and the syntax is simple.
      // More straightforward to just write the comma-separated values to the file directly, 
      // rather than creating a separate object in memory.
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH), StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(name + "," + number);
            writer.newLine();
        // Print errors for debugging
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    // Search contacts within csv file
    private static void searchContact() {
        String searchString = promptForName();
        List<String> matches = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
              // Make lower case to facilitate simple matching.
                if (line.toLowerCase().contains(searchString.toLowerCase())) {
                    matches.add(line);
                }
            }
        // print errors for debugging
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        // Console output
        if (matches.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            System.out.println("Matches found:");
            // print out each match
            matches.forEach(System.out::println);
        }
    }
}
