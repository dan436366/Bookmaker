package org.example;

public class TotalBet {
    public final int totalValue;
    public final double odds;
    public final boolean isWinTotal;

    public TotalBet(int totalValue, double odds, boolean isWin) {
        this.totalValue = totalValue;
        this.odds = odds;
        this.isWinTotal = isWin;
    }
}