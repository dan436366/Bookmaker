package org.example;

public class QuarterBet {
    private final int quarter;
    private final double team1Odds;
    private final double team2Odds;
    private final boolean team1Win;
    private final boolean team2Win;

    public QuarterBet(int quarter, double team1Odds, double team2Odds, boolean team1Win, boolean team2Win) {
        this.quarter = quarter;
        this.team1Odds = team1Odds;
        this.team2Odds = team2Odds;
        this.team1Win = team1Win;
        this.team2Win = team2Win;
    }

    public int getQuarter() {
        return quarter;
    }

    public double getTeam1Odds() {
        return team1Odds;
    }

    public double getTeam2Odds() {
        return team2Odds;
    }

    public boolean isTeam1Win() {
        return team1Win;
    }

    public boolean isTeam2Win() {
        return team2Win;
    }
}
