package org.example;

public class HandicapBet {
    private final int handicapValue;
    private final double odds;
    public final boolean isWinHandicap;

    private HandicapBet(HandicapBetBuilder handicapBetBuilder) {
        this.handicapValue = handicapBetBuilder.handicapValue;
        this.odds = handicapBetBuilder.odds;
        this.isWinHandicap = handicapBetBuilder.isWinHandicap;
    }

    public static class HandicapBetBuilder {
        private int handicapValue;
        private double odds;
        private boolean isWinHandicap;

        public HandicapBetBuilder handicapValue(int value) {
            this.handicapValue = value;
            return this;
        }

        public HandicapBetBuilder odds(double odds) {
            this.odds = odds;
            return this;
        }

        public HandicapBetBuilder isWinHandicap(boolean isWin) {
            this.isWinHandicap = isWin;
            return this;
        }

        public HandicapBet build() {
            return new HandicapBet(this);
        }
    }

    public int getHandicapValue() {
        return handicapValue;
    }

    public double getOdds() {
        return odds;
    }
}
