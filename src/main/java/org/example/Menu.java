package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.example.BettingShell.*;

// Composite class to handle multiple commands
public class Menu implements Command {
    private final List<Command> commands = new ArrayList<>();
    private final String name;
    private final Scanner scanner = new Scanner(System.in);
    private static double agencyProfit = 0.0;

    public Menu(String name) {
        this.name = name;
    }

    public void add(Command c) {
        commands.add(c);
    }

    public static void addProfit(double profit) {
        agencyProfit += profit;
    }

    public static double getAgencyProfit() {
        return agencyProfit;
    }

    @Override
    public void execute() {
        while (true) {
            System.out.println("\n=== " + name + " ===");
            for (int i = 0; i < commands.size(); i++) {
                System.out.println((i + 1) + ") " + commands.get(i).name());
            }
            System.out.println("0) Exit");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }
            if (choice > 0 && choice <= commands.size()) {
                commands.get(choice - 1).execute();
            } else {
                System.out.println("Error: Invalid command number! Please select a valid option.");
            }
        }
    }

    @Override
    public String name() {
        return name;
    }
}
