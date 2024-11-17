package org.example;

public class QuarterBet {
    private final int quarter;
    private final double team1Odds;
    private final double team2Odds;
    private final boolean team1Win;
    private final boolean team2Win;

    private QuarterBet(QuarterBuilder quarterBuilder) {
        this.quarter = quarterBuilder.quarter;
        this.team1Odds = quarterBuilder.team1Odds;
        this.team2Odds = quarterBuilder.team2Odds;
        this.team1Win = quarterBuilder.team1Win;
        this.team2Win = quarterBuilder.team2Win;
    }

    public static class QuarterBuilder {
        private int quarter;
        private double team1Odds;
        private double team2Odds;
        private boolean team1Win;
        private boolean team2Win;

        public QuarterBuilder quarter(int quarter) {
            this.quarter = quarter;
            return this;
        }

        public QuarterBuilder team1Odds(double odds) {
            this.team1Odds = odds;
            return this;
        }

        public QuarterBuilder team2Odds(double odds) {
            this.team2Odds = odds;
            return this;
        }

        public QuarterBuilder team1Win(boolean win) {
            this.team1Win = win;
            return this;
        }

        public QuarterBuilder team2Win(boolean win) {
            this.team2Win = win;
            return this;
        }

        public QuarterBet build() {
            return new QuarterBet(this);
        }
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
