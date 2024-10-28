package org.example;

import java.util.Random;

public class Match {
    private String team1;
    private String team2;
    public int team1Score;
    public int team2Score;
    public int[] team1QuarterScores;
    public int[] team2QuarterScores;
    public double team1Odds;
    public double team2Odds;
    public TotalBet[] totalBets;
    public HandicapBet[] team1HandicapBets;
    public HandicapBet[] team2HandicapBets;
    public QuarterBet[] quarterBets;
    public String winningTeam;

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    // Inner class for Total Bets
    public static class TotalBet {
        public final int totalValue;
        public final double odds;
        public final boolean isWin;

        public TotalBet(int totalValue, double odds, boolean isWin) {
            this.totalValue = totalValue;
            this.odds = odds;
            this.isWin = isWin;
        }
    }

    // Inner class for Handicap Bets
    public static class HandicapBet {
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
    public static class QuarterBet {
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

    private void generateTeamOdds() {
        Random rand = new Random();
        this.team1Odds = Math.round((1.5 + rand.nextDouble() * 1.5) * 100.0) / 100.0;
        this.team2Odds = Math.round((1.5 + rand.nextDouble() * 1.5) * 100.0) / 100.0;
    }

    // Total
    public void calculateTotalScores() {
        this.team1Score = 0;
        this.team2Score = 0;

        for (int i = 0; i < 4; i++) {
            this.team1Score += this.team1QuarterScores[i];
            this.team2Score += this.team2QuarterScores[i];
        }
    }

    private void generateTotalBets() {
        Random rand = new Random();
        int actualTotal = team1Score + team2Score;
        this.totalBets = new TotalBet[5];

        int[] totalValues = {
                actualTotal - rand.nextInt(12,15),
                actualTotal - rand.nextInt(5,8),
                actualTotal - rand.nextInt(1,3),
                actualTotal + rand.nextInt(5,8),
                actualTotal + rand.nextInt(15,17)
        };

        for (int i = totalValues.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);

            int temp = totalValues[i];
            totalValues[i] = totalValues[j];
            totalValues[j] = temp;
        }

        for (int i = 0; i < 5; i++) {
            double odds = Math.round((1.5 + rand.nextDouble() * 1.5) * 100.0) / 100.0;
            boolean isWin = actualTotal > totalValues[i];
            totalBets[i] = new TotalBet(totalValues[i], odds, isWin);
        }
    }
    // end Total

    //handicap
    private void generateHandicapBets() {
        Random rand = new Random();
        int scoreDiff = team1Score - team2Score;
        String winningTeam = scoreDiff > 0 ? team1 : team2;
        int absDiff = Math.abs(scoreDiff);

        team1HandicapBets = new HandicapBet[5];
        team2HandicapBets = new HandicapBet[5];

        //first team handicaps
        int[] handicapsTeam1 = {
                rand.nextInt(1,3),
                rand.nextInt(4,6),
                rand.nextInt(7,9),
                rand.nextInt(10,15),
                rand.nextInt(16,20)};
        for (int i = 0; i < 5; i++) {
            boolean isTeam1Winner = team1.equals(winningTeam);
            boolean isWin = false;
            double odds;

            if (isTeam1Winner) {
                isWin = scoreDiff >= handicapsTeam1[i];
                double diffFactor = Math.abs(handicapsTeam1[i] - absDiff) / 10.0;
                odds = 1.5 + diffFactor + rand.nextDouble() * 0.5;
            } else {
                isWin = false;
                odds = 2.0 + (handicapsTeam1[i] / 10.0) + rand.nextDouble() * 0.5;
            }

            team1HandicapBets[i] = new HandicapBet(handicapsTeam1[i],
                    Math.round(odds * 100.0) / 100.0, isWin);
        }

        int[] handicapsTeam2 = {
                rand.nextInt(1,3),
                rand.nextInt(4,6),
                rand.nextInt(7,9),
                rand.nextInt(10,15),
                rand.nextInt(16,20)};
        for (int i = 0; i < 5; i++) {
            boolean isTeam2Winner = team2.equals(winningTeam);
            boolean isWin = false;
            double odds;

            if (isTeam2Winner) {
                isWin = (-scoreDiff) >= handicapsTeam2[i];
                double diffFactor = Math.abs(handicapsTeam2[i] - absDiff) / 10.0;
                odds = 1.5 + diffFactor + rand.nextDouble() * 0.5;
            } else {
                isWin = false;
                odds = 2.0 + (handicapsTeam2[i] / 10.0) + rand.nextDouble() * 0.5;
            }

            team2HandicapBets[i] = new HandicapBet(handicapsTeam2[i],
                    Math.round(odds * 100.0) / 100.0, isWin);
        }
    }
    //handicap end

    // Method to process a bet
    public double processBet(String betType, int betIndex, String teamChoice, double betAmount, double userBalance) {

        double winAmount = 0;
        boolean isWin = false;

        switch (betType.toLowerCase()) {
            case "total":
                if (betIndex >= 0 && betIndex < totalBets.length) {
                    isWin = totalBets[betIndex].isWin;
                    winAmount = isWin ? betAmount * totalBets[betIndex].odds : 0;
                }
                break;
            case "handicap":
                HandicapBet[] relevantBets = teamChoice.equals(team1) ? team1HandicapBets : team2HandicapBets;
                if (betIndex >= 0 && betIndex < relevantBets.length) {
                    isWin = relevantBets[betIndex].isWin;
                    winAmount = isWin ? betAmount * relevantBets[betIndex].odds : 0;
                }
                break;
            case "quarter":
                if (betIndex >= 0 && betIndex < quarterBets.length) {
                    boolean relevantWin = teamChoice.equals(team1) ?
                            quarterBets[betIndex].team1Win : quarterBets[betIndex].team2Win;
                    double relevantOdds = teamChoice.equals(team1) ?
                            quarterBets[betIndex].team1Odds : quarterBets[betIndex].team2Odds;
                    isWin = relevantWin;
                    winAmount = isWin ? betAmount * relevantOdds : 0;
                }
                break;
            case "winlose":
                isWin = teamChoice.equals(winningTeam);
                double odds = teamChoice.equals(team1) ? team1Odds : team2Odds;
                winAmount = isWin ? betAmount * odds : 0;
                break;
            default:
                throw new IllegalArgumentException("Invalid bet type");
        }

        return winAmount;
    }
   //Method for limitation user bet
    public boolean canPlaceBet(double userBalance, double betAmount) {
        return betAmount > 0 && userBalance >= betAmount;
    }

    public Match(String team1, String team2) {
        this.team1 = team1;
        this.team2 = team2;
        initializeMatch();
    }
    //method to initialize Match
    private void initializeMatch() {
        Random rand = new Random();

        generateQuarterScores();

        calculateTotalScores();

        generateTeamOdds();

        generateTotalBets();
        generateHandicapBets();
        generateQuarterBets();

        this.winningTeam = this.team1Score > this.team2Score ? team1 : team2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Match: ").append(team1).append(" vs ").append(team2).append("\n");
        sb.append(String.format("Final Score: %s %d - %d %s\n",
                team1, team1Score, team2Score, team2));

        // Quarter scores
        for (int i = 0; i < 4; i++) {
            sb.append(String.format("Quarter %d: %d - %d\n",
                    i + 1, team1QuarterScores[i], team2QuarterScores[i]));
        }

        sb.append("\nWin/Lose: ").append(team1).append(" Odds: ").append(team1Odds)
                .append(team1.equals(winningTeam) ? " (W)" : " (L)").append(", ")
                .append(team2).append(" Odds: ").append(team2Odds)
                .append(team2.equals(winningTeam) ? " (W)" : " (L)").append("\n");

        // Total bets
        sb.append("\nTotal Bets:\n");
        for (int i = 0; i < totalBets.length; i++) {
            sb.append(String.format("Total %d (+%d): %.2f (%s)\n",
                    i + 1, totalBets[i].totalValue, totalBets[i].odds,
                    totalBets[i].isWin ? "W" : "L"));
        }

        // Handicap bets
        sb.append("\nHandicap Bets for ").append(team1).append(":\n");
        for (int i = 0; i < team1HandicapBets.length; i++) {
            sb.append(String.format("Handicap Odds %d(+%d on %s win)(%s): %.2f\n",
                    i + 1,
                    team1HandicapBets[i].handicapValue,
                    team1,
                    team1HandicapBets[i].isWin ? "W" : "L",
                    team1HandicapBets[i].odds));
        }

        sb.append("\nHandicap Bets for ").append(team2).append(":\n");
        for (int i = 0; i < team2HandicapBets.length; i++) {
            sb.append(String.format("Handicap Odds %d(+%d on %s win)(%s): %.2f\n",
                    i + 1,
                    team2HandicapBets[i].handicapValue,
                    team2,
                    team2HandicapBets[i].isWin ? "W" : "L",
                    team2HandicapBets[i].odds));
        }

        // Quarter bets
        sb.append("\nQuarter Bets:\n");
        for (QuarterBet qBet : quarterBets) {
            sb.append(String.format("Quarter %d: %s %.2f (%s) - %s %.2f (%s)\n",
                    qBet.quarter, team1, qBet.team1Odds, qBet.team1Win ? "W" : "L",
                    team2, qBet.team2Odds, qBet.team2Win ? "W" : "L"));
        }

        return sb.toString();
    }
}
