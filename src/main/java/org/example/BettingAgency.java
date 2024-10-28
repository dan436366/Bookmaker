package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BettingAgency {
    private static final Random random = new Random();
    private static final String[] TEAMS = {"Lakers", "Warriors", "Celtics", "Nets", "Heat"};
    private static final String[] USERS = {"Alice", "Bob", "Charlie", "Pes Patron"};

    public static void main(String[] args) {
        List<Match> matches = generateMatches();
        List<User> users = generateUsers();
        double agencyProfit = 0;

        for (Match match : matches) {
            System.out.println("\n=== New Match ===");

            System.out.println(match);

            System.out.println("Balances before match:");
            for (User user1 : users) {
                System.out.printf("%s: $%.2f\n", user1.getName(), user1.getBalance());
            }
            System.out.println("");

            for (User user : users) {
                // Генеруємо випадкову ставку
                double betAmount = 50 + random.nextInt(81); // Ставка від 50 до 200
                String betType = getRandomBetType();
                String chosenTeam = random.nextBoolean() ? match.getTeam1() : match.getTeam2();
                int betIndex = random.nextInt(5); // Індекс для тоталів, гандикапів або чвертей

                // Перевіряємо, чи може користувач зробити ставку
                if (!match.canPlaceBet(user.getBalance(), betAmount)) {
                    System.out.printf("%s cannot place bet of %.2f (insufficient balance: %.2f)\n",
                            user.getName(), betAmount, user.getBalance());
                    continue;
                }

                try {

                    // Спочатку віднімаємо ставку з балансу користувача
                    user.placeBet(betAmount);
                    agencyProfit += betAmount;

                    // Обробляємо ставку та отримуємо виграш
                    double winAmount = match.processBet(betType, betIndex, chosenTeam, betAmount, user.getBalance());



                    if(betType.equals("winlose")){
                        System.out.printf("%s placed %s on %s with amount %.2f\n",
                                user.getName(), betType, chosenTeam, betAmount);
                    }
                    else if(betType.equals("total")){
                        System.out.printf("%s placed %s %d with amount %.2f\n",
                                user.getName(), betType, betIndex + 1, betAmount);
                    }
                    else{
                        System.out.printf("%s placed %s %d on %s with amount %.2f\n",
                                user.getName(), betType, betIndex + 1, chosenTeam, betAmount);
                    }



                    if (winAmount > 0) {
                        user.winBet(winAmount);
                        agencyProfit -= winAmount;
                        System.out.printf("%s won: %.2f\n", user.getName(), winAmount);
                    } else {
                        System.out.printf("%s lost: %.2f\n", user.getName(), betAmount);
                    }

                } catch (IllegalStateException | IllegalArgumentException e) {
                    // Повертаємо ставку у випадку помилки
                    user.winBet(betAmount);
                    agencyProfit -= betAmount;
                    System.out.printf("Error processing bet for %s: %s\n",
                            user.getName(), e.getMessage());
                }
            }

            // Виводимо баланси користувачів після матчу
            System.out.println("\nBalances after match:");
            for (User user : users) {
                System.out.printf("%s: $%.2f\n", user.getName(), user.getBalance());
            }
        }

        // Виводимо фінальний прибуток букмекерської контори
        System.out.printf("\nFinal agency profit: $%.2f\n", agencyProfit);
    }

    private static List<Match> generateMatches() {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String team1 = TEAMS[random.nextInt(TEAMS.length)];
            String team2;
            do {
                team2 = TEAMS[random.nextInt(TEAMS.length)];
            } while (team1.equals(team2));

            matches.add(new Match(team1, team2));
        }
        return matches;
    }

    private static List<User> generateUsers() {
        List<User> users = new ArrayList<>();
        for (String userName : USERS) {
            users.add(new User(userName, 100 + random.nextInt(401))); // Баланс від 100 до 1000
        }
        return users;
    }

    private static String getRandomBetType() {
        String[] betTypes = {"total", "handicap", "quarter", "winlose"};
        return betTypes[random.nextInt(betTypes.length)];
    }
}
