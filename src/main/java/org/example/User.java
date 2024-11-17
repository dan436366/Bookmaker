package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class User {
    private static final Logger logger = LogManager.getLogger(User.class);
    private final String name;
    private double balance;

    private User(UserBuilder userBuilder) {
        this.name = userBuilder.name;
        this.balance = userBuilder.balance;
        logger.info("User created: Name={}, Balance={}", name, balance);
    }

    public static class UserBuilder {
        private String name;
        private double balance;

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder balance(double balance) {
            this.balance = balance;
            return this;
        }

        public User build() {
            return new User(this);
        }
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
