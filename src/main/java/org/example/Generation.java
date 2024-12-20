package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Generation {

    private static Random random = new Random();

    public static void setRandom(Random newRandom) {
        random = newRandom;
    }

    public static int[] generateQuarterScores(int minScore, int maxScore) {
        int[] quarterScores = new int[4];
        for (int i = 0; i < 4; i++) {
            quarterScores[i] = minScore + random.nextInt(maxScore - minScore + 1);
        }
        return quarterScores;
    }

    public static int[] generateTotals(int[] bounds) {
        int[] totals = new int[bounds.length / 2];
        int m = 0;
        int n = m+1;
        for (int i = 0; i < totals.length; i++) {
            totals[i] =  random.nextInt(bounds[m], bounds[n]);
            m+=2;
            n+=2;
        }
        return totals;
    }

    public static int calculateTotalScore(int[] quarterScores) {
        int totalScore = 0;
        for (int score : quarterScores) {
            totalScore += score;
        }
        return totalScore;
    }

    public static double generateTeamOdds(double minOdds, double maxOdds) {
        return Math.round((minOdds + random.nextDouble() * (maxOdds - minOdds)) * 100.0) / 100.0;
    }

    public static TotalBet[] generateTotalBets(int actualTotal, int[] totalBetValues, double minOdds, double maxOdds) {
        TotalBet[] totalBets = new TotalBet[totalBetValues.length];
        for (int i = 0; i < totalBetValues.length; i++) {
            int totalValue = actualTotal + totalBetValues[i];
            double odds = minOdds + random.nextDouble() * (maxOdds - minOdds);
            boolean isWin = actualTotal > totalValue;
            totalBets[i] = new TotalBet.TotalBetBuilder().totalValue(totalValue).odds(odds).isWinTotal(isWin).build();
        }
        return totalBets;
    }

    public static HandicapBet[] generateHandicapBets(String winningTeam, int teamScore, int opponentScore, int quantityOfHandicaps,
                                                     int minHandicapValue, int maxHandicapValue, int absDiff, double minOdds, double maxOdds) {
        Set<Integer> generatedValues = new TreeSet<>();
        while (generatedValues.size() < quantityOfHandicaps) {
            int handicapValue = minHandicapValue + random.nextInt(maxHandicapValue - minHandicapValue + 1);
            generatedValues.add(handicapValue);
        }

        HandicapBet[] handicapBets = new HandicapBet[quantityOfHandicaps];
        int index = 0;
        for (int handicapValue : generatedValues) {
            boolean isWin = (teamScore - opponentScore - handicapValue) >= 0;
            double odds = calculateHandicapOdds(isWin, handicapValue, absDiff, minOdds, maxOdds);
            handicapBets[index++] = new HandicapBet.HandicapBetBuilder().handicapValue(handicapValue).odds(odds).isWinHandicap(isWin).build();
        }
        return handicapBets;
    }

    private static double calculateHandicapOdds(boolean isWinner, int handicapValue, int absDiff, double minOdds, double maxOdds) {
        double diffFactor = Math.abs(handicapValue - absDiff) / 10.0;
        return isWinner ? minOdds + diffFactor + random.nextDouble() * (maxOdds - minOdds - diffFactor)
                : minOdds + (handicapValue / 10.0) + random.nextDouble() * (maxOdds - minOdds - (handicapValue / 10.0));
    }

    public static QuarterBet[] generateQuarterBets(int[] team1QuarterScores, int[] team2QuarterScores, double minOdds, double maxOdds) {
        QuarterBet[] quarterBets = new QuarterBet[4];
        for (int i = 0; i < 4; i++) {
            double team1Odds = minOdds + random.nextDouble() * (maxOdds - minOdds);
            double team2Odds = minOdds + random.nextDouble() * (maxOdds - minOdds);
            boolean team1Win = team1QuarterScores[i] > team2QuarterScores[i];
            boolean team2Win = team2QuarterScores[i] > team1QuarterScores[i];
            quarterBets[i] = new QuarterBet.QuarterBuilder().quarter(i + 1).team1Odds(team1Odds).team2Odds(team2Odds).team1Win(team1Win).team2Win(team2Win).build();
        }
        return quarterBets;
    }

    public static List<Match> generateMatches(String[] teams, int numOfMatches) {
        List<Match> matches = new ArrayList<>();
        for (int i = 0; i < numOfMatches; i++) {
            String team1 = teams[random.nextInt(teams.length)];
            String team2;
            do {
                team2 = teams[random.nextInt(teams.length)];
            } while (team1.equals(team2));
            matches.add(new Match.MatchBuilder().team1(team1).team2(team2).build());
        }
        return matches;
    }

    public static List<User> generateUsers(String[] users, int minBalance, int maxBalance) {
        List<User> userList = new ArrayList<>();
        for (String userName : users) {
            userList.add(new User.UserBuilder().name(userName).balance(minBalance + random.nextInt(maxBalance - minBalance + 1)).build());
        }
        return userList;
    }
}
