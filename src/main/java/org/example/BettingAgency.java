package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BettingAgency {
    private static final Logger logger = LogManager.getLogger(BettingAgency.class);
    public static final Random random = new Random();
    private static final String[] TEAMS = {"Lakers", "Warriors", "Celtics", "Nets", "Heat"};
    private static final String[] USERS = {"Alice", "Bob", "Charlie", "Pes Patron"};
    public static final int NUM_OF_MATCHES = 5;
    private static final int MIN_INITIAL_BALANCE = 100;
    private static final int MAX_INITIAL_BALANCE = 1000;
    private static final int MIN_BET_AMOUNT = 50;
    private static final int MAX_BET_AMOUNT = 200;
    public static final int[] TOTAL_BET_VALUES = {
            random.nextInt(-15,-12),
            random.nextInt(-11,-6),
            random.nextInt(-5,-1),
            random.nextInt(1,6),
            random.nextInt(7,15)};
    public static final int MIN_QUARTER_SCORE = 20;
    public static final int MAX_QUARTER_SCORE = 35;
    public static final int MIN_HANDICAP_VALUE = 1;
    public static final int MAX_HANDICAP_VALUE = 20;
    public static final int QUANTITY_OF_HANDICAPS = 5;
    public static final double MIN_ODDS = 1.1;
    public static final double MAX_ODDS = 3.0;


    public static void main(String[] args) {
        logger.info("Starting Betting Agency simulation...");
        List<Match> matches = Generation.generateMatches(TEAMS, NUM_OF_MATCHES);
        List<User> users = Generation.generateUsers(USERS, MIN_INITIAL_BALANCE, MAX_INITIAL_BALANCE);
        double agencyProfit = 0;

        for (Match match : matches) {
            System.out.println("\n=== New Match ===");
            System.out.println(match);

            System.out.println("Balances before match:");
            for (User user : users) {
                System.out.printf("%s: $%.2f\n", user.getName(), user.getBalance());
            }
            System.out.println("");

            for (User user : users) {
                double betAmount = MIN_BET_AMOUNT + random.nextInt(MAX_BET_AMOUNT - MIN_BET_AMOUNT + 1);
                String betType = getRandomBetType();
                String chosenTeam = random.nextBoolean() ? match.getTeam1() : match.getTeam2();

                int betIndexTotal = random.nextInt(TOTAL_BET_VALUES.length);
                int betIndexHandicap = random.nextInt(QUANTITY_OF_HANDICAPS);
                int betIndexQuarter = random.nextInt(4);

                if (match.canPlaceBet(user.getBalance(), betAmount)) {
                    try {
                        user.placeBet(betAmount);
                        agencyProfit += betAmount;
                        double winAmount = 0;

                        if(betType.equals("winlose")){
                            System.out.printf("%s placed %s on %s with amount %.2f\n",
                                    user.getName(), betType, chosenTeam, betAmount);
                            winAmount = match.processBet(betType, 0, chosenTeam, betAmount, user.getBalance());
                        }
                        else if(betType.equals("total")){
                            System.out.printf("%s placed %s %d with amount %.2f\n",
                                    user.getName(), betType, betIndexTotal + 1, betAmount);
                            winAmount = match.processBet(betType, betIndexTotal, chosenTeam, betAmount, user.getBalance());
                        }
                        else if(betType.equals("quarter")){
                            System.out.printf("%s placed %s %d on %s with amount %.2f\n",
                                    user.getName(), betType, betIndexQuarter + 1, chosenTeam, betAmount);
                            winAmount = match.processBet(betType, betIndexQuarter, chosenTeam, betAmount, user.getBalance());
                        }
                        else if(betType.equals("handicap")){
                            System.out.printf("%s placed %s %d on %s with amount %.2f\n",
                                    user.getName(), betType, betIndexHandicap + 1, chosenTeam, betAmount);
                            winAmount = match.processBet(betType, betIndexHandicap, chosenTeam, betAmount, user.getBalance());
                        }


                        if (winAmount > 0) {
                            user.winBet(winAmount);
                            agencyProfit -= winAmount;
                            logger.info("{} won ${} with bet type {}", user.getName(), winAmount, betType);
                            System.out.printf("%s won: %.2f\n", user.getName(), winAmount);
                        } else {
                            logger.info("{} lost ${} with bet type {}", user.getName(), betAmount, betType);
                            System.out.printf("%s lost: %.2f\n", user.getName(), betAmount);
                        }
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        user.winBet(betAmount);
                        agencyProfit -= betAmount;
                        logger.error("Error processing bet for {}: {}", user.getName(), e.getMessage(), e);
                        System.out.printf("Error processing bet for %s: %s\n", user.getName(), e.getMessage());
                    }
                } else {
                    logger.warn("{} cannot place bet of ${}. Insufficient balance: ${}", user.getName(), betAmount, user.getBalance());
                    System.out.printf("%s cannot place bet of %.2f (insufficient balance: %.2f)\n", user.getName(), betAmount, user.getBalance());
                }
            }

            System.out.println("\nBalances after match:");
            for (User user : users) {
                System.out.printf("%s: $%.2f\n", user.getName(), user.getBalance());
            }
        }

        logger.info("Final agency profit: ${}", agencyProfit);
        System.out.printf("\nFinal agency profit: $%.2f\n", agencyProfit);
    }

    private static String getRandomBetType() {
        String[] betTypes = {"total", "handicap", "quarter", "winlose"};
        return betTypes[random.nextInt(betTypes.length)];
    }
}