package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Leaf class for manual betting
public class ManualBettingCommand implements Command {
    private final Scanner scanner = new Scanner(System.in);
    private List<User> users = new ArrayList<>();
    private List<Match> matches;

    @Override
    public void execute() {
        Menu manualMenu = new Menu("Manual Betting Menu");
        manualMenu.add(new Command() {
            @Override
            public void execute() {
                createUser();
            }

            @Override
            public String name() {
                return "Create User";
            }
        });

        manualMenu.add(new Command() {
            @Override
            public void execute() {
                if (users.isEmpty()) {
                    System.out.println("Please create at least one user first!");
                    return;
                }
                placeBet();
            }

            @Override
            public String name() {
                return "Place Bet";
            }
        });

        manualMenu.execute();
    }

    private void createUser() {
        System.out.print("Enter user name: ");
        String name = scanner.next();
        System.out.print("Enter initial balance: ");
        double balance = scanner.nextDouble();
        users.add(new User(name, balance));
        System.out.println("User created successfully!");
    }

    private void placeBet() {
        matches = Generation.generateMatches(BettingAgency.TEAMS, 3);

        // Select user
        System.out.println("\nSelect user:");
        for (int i = 0; i < users.size(); i++) {
            System.out.printf("%d) %s (Balance: $%.2f)\n", i + 1, users.get(i).getName(), users.get(i).getBalance());
        }
        int userChoice = scanner.nextInt() - 1;

        if (userChoice < 0 || userChoice >= users.size()) {
            System.out.println("Error: Invalid user selection!");
            return;
        }

        User selectedUser = users.get(userChoice);

        // Select match
        System.out.println("\nSelect match:");
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            System.out.printf("%d) %s vs %s\n", i + 1, match.getTeam1(), match.getTeam2());
        }
        int matchChoice = scanner.nextInt() - 1;

        if (matchChoice < 0 || matchChoice >= matches.size()) {
            System.out.println("Error: Invalid match selection!");
            return;
        }

        Match selectedMatch = matches.get(matchChoice);

        System.out.println("\nMatch Details:");
        System.out.println(getMatchInfoWithoutWL(selectedMatch));

        System.out.println("\nSelect bet type:");
        System.out.println("1) Win/Lose");
        System.out.println("2) Total");
        System.out.println("3) Handicap");
        System.out.println("4) Quarter");
        int betTypeChoice = scanner.nextInt();

        String betType;
        try {
            betType = switch (betTypeChoice) {
                case 1 -> "winlose";
                case 2 -> "total";
                case 3 -> "handicap";
                case 4 -> "quarter";
                default -> throw new IllegalStateException("Invalid bet type");
            };
        } catch (IllegalStateException e) {
            System.out.println("Error: Invalid bet type selection!");
            return;
        }

        String selectedTeam = selectedMatch.getTeam1();
        if (betType.equals("winlose") || betType.equals("handicap") || betType.equals("quarter")) {
            System.out.println("\nSelect team:");
            System.out.println("1) " + selectedMatch.getTeam1());
            System.out.println("2) " + selectedMatch.getTeam2());
            int teamChoice = scanner.nextInt();

            if (teamChoice != 1 && teamChoice != 2) {
                System.out.println("Error: Invalid team selection!");
                return;
            }

            selectedTeam = teamChoice == 1 ? selectedMatch.getTeam1() : selectedMatch.getTeam2();
        }

        System.out.print("\nEnter bet amount: ");
        double betAmount = scanner.nextDouble();

        if (selectedMatch.canPlaceBet(selectedUser.getBalance(), betAmount)) {
            selectedUser.placeBet(betAmount);
            int betIndex = 0;
            if (!betType.equals("winlose")) {
                if (betType.equals("quarter")) {
                    System.out.print("Enter bet index (1-4): ");
                    betIndex = scanner.nextInt() - 1;
                    if (betIndex < 0 || betIndex >= 4) {
                        System.out.println("Error: Invalid quarter index!");
                        selectedUser.winBet(betAmount);
                        return;
                    }
                } else {
                    System.out.print("Enter bet index (1-5): ");
                    betIndex = scanner.nextInt() - 1;
                    if (betIndex < 0 || betIndex >= 5) {
                        System.out.println("Error: Invalid bet index!");
                        selectedUser.winBet(betAmount);
                        return;
                    }
                }
            }

            double winAmount = selectedMatch.processBet(betType, betIndex, selectedTeam, betAmount, selectedUser.getBalance());

            if (winAmount > 0) {
                selectedUser.winBet(winAmount);
                Menu.addProfit(-(winAmount - betAmount)); // Agency loses the winnings but keeps the initial bet
                System.out.printf("%s won: $%.2f\n", selectedUser.getName(), winAmount - betAmount);
            } else {
                Menu.addProfit(betAmount); // Agency keeps the entire bet amount
                System.out.printf("%s lost: $%.2f\n", selectedUser.getName(), betAmount);
            }

            System.out.printf("\nUpdated balance for %s: $%.2f\n", selectedUser.getName(), selectedUser.getBalance());
            System.out.println("\nMatch Results:");
            System.out.println(selectedMatch);
        } else {
            System.out.println("Insufficient balance for this bet!");
        }
    }

    private String getMatchInfoWithoutWL(Match match) {
        StringBuilder sb = new StringBuilder();
        sb.append("Match: ").append(match.getTeam1()).append(" vs ").append(match.getTeam2()).append("\n");

        sb.append("\nWin/Lose Odds: \n")
                .append(match.getTeam1()).append(" Odds: ").append(match.team1Odds).append("\n")
                .append(match.getTeam2()).append(" Odds: ").append(match.team2Odds).append("\n");

        sb.append("\nTotal Bets:\n");
        for (int i = 0; i < match.totalBets.length; i++) {
            sb.append(String.format("Total %d (+%d): %.2f\n",
                    i + 1, match.totalBets[i].totalValue, match.totalBets[i].odds));
        }

        sb.append("\nHandicap Bets for ").append(match.getTeam1()).append(":\n");
        for (int i = 0; i < match.team1HandicapBets.length; i++) {
            sb.append(String.format("Handicap %d(+%d): %.2f\n",
                    i + 1,
                    match.team1HandicapBets[i].getHandicapValue(),
                    match.team1HandicapBets[i].getOdds()));
        }

        sb.append("\nHandicap Bets for ").append(match.getTeam2()).append(":\n");
        for (int i = 0; i < match.team2HandicapBets.length; i++) {
            sb.append(String.format("Handicap %d(+%d): %.2f\n",
                    i + 1,
                    match.team2HandicapBets[i].getHandicapValue(),
                    match.team2HandicapBets[i].getOdds()));
        }

        sb.append("\nQuarter Bets:\n");
        for (QuarterBet qBet : match.quarterBets) {
            sb.append(String.format("Quarter %d: %s %.2f - %s %.2f\n",
                    qBet.getQuarter(), match.getTeam1(), qBet.getTeam1Odds(),
                    match.getTeam2(), qBet.getTeam2Odds()));
        }

        return sb.toString();
    }

    @Override
    public String name() {
        return "Manual Betting";
    }
}