package org.example;

import java.util.Random;

public class Match {
    private String team1;
    private String team2;
    private int team1Score;
    private int team2Score;
    private int[] team1QuarterScores;
    private int[] team2QuarterScores;
    private double team1Odds;
    private double team2Odds;
    private TotalBet[] totalBets;
    private HandicapBet[] team1HandicapBets;
    private HandicapBet[] team2HandicapBets;
    private QuarterBet[] quarterBets;
    private String winningTeam;

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public double getTeam1Odds() {
        return team1Odds;
    }

    public double getTeam2Odds() {
        return team2Odds;
    }

    // Inner class for Total Bets
    private static class TotalBet {
        private final int totalValue;
        private final double odds;
        private final boolean isWin;

        public TotalBet(int totalValue, double odds, boolean isWin) {
            this.totalValue = totalValue;
            this.odds = odds;
            this.isWin = isWin;
        }
    }

    // Inner class for Handicap Bets
    private static class HandicapBet {
        private final int handicapValue;
        private final double odds;
        private final boolean isWin;

        public HandicapBet(int handicapValue, double odds, boolean isWin) {
            this.handicapValue = handicapValue;
            this.odds = odds;
            this.isWin = isWin;
        }
    }

    // Inner class for Quarter Bets
    private static class QuarterBet {
        private final int quarter;
        private final double team1Odds;
        private final double team2Odds;
        private final boolean team1Win;
        private final boolean team2Win;

        public QuarterBet(int quarter, double team1Odds, double team2Odds,
                          boolean team1Win, boolean team2Win) {
            this.quarter = quarter;
            this.team1Odds = team1Odds;
            this.team2Odds = team2Odds;
            this.team1Win = team1Win;
            this.team2Win = team2Win;
        }
    }

    //quarter
    private void generateQuarterScores() {
        Random rand = new Random();
        this.team1QuarterScores = new int[4];
        this.team2QuarterScores = new int[4];


        for (int i = 0; i < 4; i++) {
            // More realistic basketball quarter scores (20-35 points per quarter)
            this.team1QuarterScores[i] = 20 + rand.nextInt(16);
            this.team2QuarterScores[i] = 20 + rand.nextInt(16);
        }
    }

    private void generateQuarterBets() {
        Random rand = new Random();
        this.quarterBets = new QuarterBet[4];

        for (int i = 0; i < 4; i++) {
            double team1Odds = Math.round((1.5 + rand.nextDouble() * 1.5) * 100.0) / 100.0;
            double team2Odds = Math.round((1.5 + rand.nextDouble() * 1.5) * 100.0) / 100.0;
            boolean team1Win = team1QuarterScores[i] > team2QuarterScores[i];
            boolean team2Win = team2QuarterScores[i] > team1QuarterScores[i];

            quarterBets[i] = new QuarterBet(i + 1, team1Odds, team2Odds, team1Win, team2Win);
        }
    }
    //quarter end

}
