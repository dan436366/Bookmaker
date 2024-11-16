package org.example;

public class BettingShell{
    public static void main(String[] args) {
        Menu mainMenu = new Menu("Betting System");
        mainMenu.add(new DemoCommand());
        mainMenu.add(new ManualBettingCommand());
        mainMenu.add(new CheckProfitCommand());
        mainMenu.execute();
    }
}