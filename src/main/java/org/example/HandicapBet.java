package org.example;

public class HandicapBet {
    private final int handicapValue;
    private final double odds;
    public final boolean isWinHandicap;

    public HandicapBet(int handicapValue, double odds, boolean isWin) {
        this.handicapValue = handicapValue;
        this.odds = odds;
        this.isWinHandicap = isWin;
    }

    public int getHandicapValue() {
        return handicapValue;
    }

    public double getOdds() {
        return odds;
    }
}
