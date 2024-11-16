package org.example;

// Leaf class for checking agency profit
public class CheckProfitCommand implements Command {
    @Override
    public void execute() {
        System.out.printf("\nCurrent Agency Profit: $%.2f%n", Menu.getAgencyProfit());
    }

    @Override
    public String name() {
        return "Check Agency Profit";
    }
}