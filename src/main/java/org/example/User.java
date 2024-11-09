//package org.example;
//
//public class User {
//    public String name;
//    public double balance;
//
//    public User(String name, double balance) {
//        this.name = name;
//        this.balance = balance;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public double getBalance() {
//        return balance;
//    }
//
//    public void placeBet(double amount) {
//        balance -= amount;
//    }
//
//    public void winBet(double winnings) {
//        balance += winnings;
//    }
//}



package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User {
    private static final Logger logger = LogManager.getLogger(User.class);
    private String name;
    private double balance;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        logger.info("User created: Name={}, Balance={}", name, balance);
    }

    public void placeBet(double amount) {
        balance -= amount;
        logger.debug("{} placed a bet of ${}. New balance: ${}", name, amount, balance);
    }

    public void winBet(double winnings) {
        balance += winnings;
        logger.debug("{} won ${}. New balance: ${}", name, winnings, balance);
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }
}
