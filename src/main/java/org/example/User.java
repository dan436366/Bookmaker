package org.example;

public class User {
    private String name;
    private double balance;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void placeBet(double amount) {
        balance -= amount;
    }

    public void winBet(double winnings) {
        balance += winnings;
    }
}
