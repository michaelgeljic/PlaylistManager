package ui;

import java.util.Scanner;

/**
 * A simple console-based menu that can be used for non-GUI interaction.
 * Provides basic commands for searching and selecting songs from the terminal.
 */
public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);

    public void start() {

        while (true) {
            System.out.println("\n=== Music Library ===");
            System.out.println("1. Search Song");
            System.out.println("2. View Related Artists");
            System.out.println("3. Update Song");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> System.out.println("TODO: Implement Song Search");
                case "2" -> System.out.println("TODO: Implement Related Artists");
                case "3" -> System.out.println("TODO: Implement Update Song");
                case "4" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }
}
