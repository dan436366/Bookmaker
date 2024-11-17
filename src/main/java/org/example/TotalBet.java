package org.example;

public class TotalBet {
    public final int totalValue;
    public final double odds;
    public final boolean isWinTotal;

    private TotalBet(TotalBetBuilder totalBetBuilder) {
        this.totalValue = totalBetBuilder.totalValue;
        this.odds = totalBetBuilder.odds;
        this.isWinTotal = totalBetBuilder.isWinTotal;
    }

    public static class TotalBetBuilder {
        private int totalValue;
        private double odds;
        private boolean isWinTotal;

        public TotalBetBuilder totalValue(int value) {
            this.totalValue = value;
            return this;
        }

        public TotalBetBuilder odds(double odds) {
            this.odds = odds;
            return this;
        }

        public TotalBetBuilder isWinTotal(boolean isWin) {
            this.isWinTotal = isWin;
            return this;
        }

        public TotalBet build() {
            return new TotalBet(this);
        }
    }
}