package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        users.add(new User.UserBuilder().name(name).balance(balance).build());

        System.out.println("User created successfully!");
    }

    private void placeBet() {
        matches = Generation.generateMatches(BettingAgency.TEAMS, 3);
        
        User selectedUser = selectUser();
        if (selectedUser == null) return;

        Match selectedMatch = selectMatch();
        if (selectedMatch == null) return;

        System.out.println("\nMatch Details:");
        System.out.println(getMatchInfoWithoutWL(selectedMatch));

        String betType = selectBetType();
        if (betType == null) return;

        String selectedTeam = selectTeam(selectedMatch, betType);
        if (selectedTeam == null && needsTeamSelection(betType)) return;

        double betAmount = getBetAmount();
        if (!selectedMatch.canPlaceBet(selectedUser.getBalance(), betAmount)) {
            System.out.println("Insufficient balance for this bet!");
            return;
        }

        processBetAndShowResults(selectedUser, selectedMatch, betType, selectedTeam, betAmount);
    }

    private User selectUser() {
        System.out.println("\nSelect user:");
        for (int i = 0; i < users.size(); i++) {
            System.out.printf("%d) %s (Balance: $%.2f)\n", i + 1, users.get(i).getName(), users.get(i).getBalance());
        }
        int userChoice = scanner.nextInt() - 1;

        if (userChoice < 0 || userChoice >= users.size()) {
            System.out.println("Error: Invalid user selection!");
            return null;
        }
        return users.get(userChoice);
    }

    private Match selectMatch() {
        System.out.println("\nSelect match:");
        for (int i = 0; i < matches.size(); i++) {
            Match match = matches.get(i);
            System.out.printf("%d) %s vs %s\n", i + 1, match.getTeam1(), match.getTeam2());
        }
        int matchChoice = scanner.nextInt() - 1;

        if (matchChoice < 0 || matchChoice >= matches.size()) {
            System.out.println("Error: Invalid match selection!");
            return null;
        }
        return matches.get(matchChoice);
    }

    private String selectBetType() {
        System.out.println("\nSelect bet type:");
        System.out.println("1) Win/Lose");
        System.out.println("2) Total");
        System.out.println("3) Handicap");
        System.out.println("4) Quarter");
        int betTypeChoice = scanner.nextInt();

        try {
            return switch (betTypeChoice) {
                case 1 -> "winlose";
                case 2 -> "total";
                case 3 -> "handicap";
                case 4 -> "quarter";
                default -> throw new IllegalStateException("Invalid bet type");
            };
        } catch (IllegalStateException e) {
            System.out.println("Error: Invalid bet type selection!");
            return null;
        }
    }

    private boolean needsTeamSelection(String betType) {
        return betType.equals("winlose") || betType.equals("handicap") || betType.equals("quarter");
    }

    private String selectTeam(Match match, String betType) {
        if (!needsTeamSelection(betType)) {
            return match.getTeam1();
        }

        System.out.println("\nSelect team:");
        System.out.println("1) " + match.getTeam1());
        System.out.println("2) " + match.getTeam2());
        int teamChoice = scanner.nextInt();

        if (teamChoice != 1 && teamChoice != 2) {
            System.out.println("Error: Invalid team selection!");
            return null;
        }
        return teamChoice == 1 ? match.getTeam1() : match.getTeam2();
    }

    private double getBetAmount() {
        System.out.print("\nEnter bet amount: ");
        return scanner.nextDouble();
    }

    private void processBetAndShowResults(User user, Match match, String betType,
                                          String selectedTeam, double betAmount) {
        user.placeBet(betAmount);
        int betIndex = getBetIndex(betType);
        if (betIndex == -1 && !betType.equals("winlose")) {
            user.winBet(betAmount);
            return;
        }

        double winAmount = match.processBet(betType, betIndex, selectedTeam, betAmount, user.getBalance());
        showBetResults(user, match, betAmount, winAmount);
    }

    private int getBetIndex(String betType) {
        if (betType.equals("winlose")) {
            return 0;
        }

        int maxIndex = betType.equals("quarter") ? 4 : 5;
        System.out.printf("Enter bet index (1-%d): ", maxIndex);
        int betIndex = scanner.nextInt() - 1;

        if (betIndex < 0 || betIndex >= maxIndex) {
            System.out.println("Error: Invalid bet index!");
            return -1;
        }
        return betIndex;
    }

    private void showBetResults(User user, Match match, double betAmount, double winAmount) {
        if (winAmount > 0) {
            user.winBet(winAmount);
            Menu.addProfit(-(winAmount - betAmount));
            System.out.printf("%s won: $%.2f\n", user.getName(), winAmount - betAmount);
        } else {
            Menu.addProfit(betAmount);
            System.out.printf("%s lost: $%.2f\n", user.getName(), betAmount);
        }

        System.out.printf("\nUpdated balance for %s: $%.2f\n", user.getName(), user.getBalance());
        System.out.println("\nMatch Results:");
        System.out.println(match);
    }

    private String getMatchInfoWithoutWL(Match match) {
        StringBuilder sb = new StringBuilder();
        appendMatchHeader(sb, match);
        appendWinLoseOdds(sb, match);
        appendTotalBets(sb, match);
        appendHandicapBets(sb, match);
        appendQuarterBets(sb, match);
        return sb.toString();
    }

    private void appendMatchHeader(StringBuilder sb, Match match) {
        sb.append("Match: ")
                .append(match.getTeam1())
                .append(" vs ")
                .append(match.getTeam2())
                .append("\n");
    }

    private void appendWinLoseOdds(StringBuilder sb, Match match) {
        sb.append("\nWin/Lose Odds: \n")
                .append(match.getTeam1()).append(" Odds: ").append(match.team1Odds).append("\n")
                .append(match.getTeam2()).append(" Odds: ").append(match.team2Odds).append("\n");
    }

    private void appendTotalBets(StringBuilder sb, Match match) {
        sb.append("\nTotal Bets:\n");
        for (int i = 0; i < match.totalBets.length; i++) {
            sb.append(String.format("Total %d (+%d): %.2f\n",
                    i + 1, match.totalBets[i].totalValue, match.totalBets[i].odds));
        }
    }

    private void appendHandicapBets(StringBuilder sb, Match match) {
        appendTeamHandicapBets(sb, match, match.getTeam1(), match.team1HandicapBets);
        appendTeamHandicapBets(sb, match, match.getTeam2(), match.team2HandicapBets);
    }

    private void appendTeamHandicapBets(StringBuilder sb, Match match,
                                        String team, HandicapBet[] handicapBets) {
        sb.append("\nHandicap Bets for ").append(team).append(":\n");
        for (int i = 0; i < handicapBets.length; i++) {
            sb.append(String.format("Handicap %d(+%d): %.2f\n",
                    i + 1,
                    handicapBets[i].getHandicapValue(),
                    handicapBets[i].getOdds()));
        }
    }

    private void appendQuarterBets(StringBuilder sb, Match match) {
        sb.append("\nQuarter Bets:\n");
        for (QuarterBet qBet : match.quarterBets) {
            sb.append(String.format("Quarter %d: %s %.2f - %s %.2f\n",
                    qBet.getQuarter(),
                    match.getTeam1(),
                    qBet.getTeam1Odds(),
                    match.getTeam2(),
                    qBet.getTeam2Odds()));
        }
    }

    @Override
    public String name() {
        return "Manual Betting";
    }
}