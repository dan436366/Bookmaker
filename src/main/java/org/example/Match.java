package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static org.example.BettingAgency.*;

public class Match {

    private static final Logger logger = LogManager.getLogger(Match.class);

    private final String team1;
    private final String team2;
    public int team1Score;
    public int team2Score;
    public int[] team1QuarterScores;
    public int[] team2QuarterScores;
    public double team1Odds;
    public double team2Odds;
    public int[] generatedNumsForTotal;
    public TotalBet[] totalBets;
    public HandicapBet[] team1HandicapBets;
    public HandicapBet[] team2HandicapBets;
    public QuarterBet[] quarterBets;
    public String winningTeam;
    public int scoreDiff = Math.abs(team1Score - team2Score);

    private Match(MatchBuilder matchBuilder) {
        this.team1 = matchBuilder.team1;
        this.team2 = matchBuilder.team2;
        this.team1QuarterScores = matchBuilder.team1QuarterScores;
        this.team2QuarterScores = matchBuilder.team2QuarterScores;
        this.team1Odds = matchBuilder.team1Odds;
        this.team2Odds = matchBuilder.team2Odds;
        this.generatedNumsForTotal = matchBuilder.generatedNumsForTotal;
        this.totalBets = matchBuilder.totalBets;
        this.team1HandicapBets = matchBuilder.team1HandicapBets;
        this.team2HandicapBets = matchBuilder.team2HandicapBets;
        this.quarterBets = matchBuilder.quarterBets;
        this.winningTeam = matchBuilder.winningTeam;
        initializeMatch();
    }

    public static class MatchBuilder {
        private String team1;
        private String team2;
        private int[] team1QuarterScores;
        private int[] team2QuarterScores;
        private double team1Odds;
        private double team2Odds;
        private int[] generatedNumsForTotal;
        private TotalBet[] totalBets;
        private HandicapBet[] team1HandicapBets;
        private HandicapBet[] team2HandicapBets;
        private QuarterBet[] quarterBets;
        private String winningTeam;

        public MatchBuilder team1(String team1) {
            this.team1 = team1;
            return this;
        }

        public MatchBuilder team2(String team2) {
            this.team2 = team2;
            return this;
        }

        public MatchBuilder team1QuarterScores(int[] scores) {
            this.team1QuarterScores = scores;
            return this;
        }

        public MatchBuilder team2QuarterScores(int[] scores) {
            this.team2QuarterScores = scores;
            return this;
        }

        public MatchBuilder team1Odds(double odds) {
            this.team1Odds = odds;
            return this;
        }

        public MatchBuilder team2Odds(double odds) {
            this.team2Odds = odds;
            return this;
        }

        public MatchBuilder totalBets(TotalBet[] bets) {
            this.totalBets = bets;
            return this;
        }

        public MatchBuilder handicapBets(HandicapBet[] team1Bets, HandicapBet[] team2Bets) {
            this.team1HandicapBets = team1Bets;
            this.team2HandicapBets = team2Bets;
            return this;
        }

        public MatchBuilder quarterBets(QuarterBet[] bets) {
            this.quarterBets = bets;
            return this;
        }

        public MatchBuilder winningTeam(String team) {
            this.winningTeam = team;
            return this;
        }

        public Match build() {
            return new Match(this);
        }
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    private void initializeMatch() {
        this.team1QuarterScores = Generation.generateQuarterScores(MIN_QUARTER_SCORE, MAX_QUARTER_SCORE);
        this.team2QuarterScores = Generation.generateQuarterScores(MIN_QUARTER_SCORE, MAX_QUARTER_SCORE);

        this.team1Score = Generation.calculateTotalScore(team1QuarterScores);
        this.team2Score = Generation.calculateTotalScore(team2QuarterScores);

        this.team1Odds = Generation.generateTeamOdds(MIN_ODDS, MAX_ODDS);
        this.team2Odds = Generation.generateTeamOdds(MIN_ODDS, MAX_ODDS);

        this.generatedNumsForTotal = Generation.generateTotals(TOTAL_BET_VALUES);

        this.totalBets = Generation.generateTotalBets(team1Score + team2Score, generatedNumsForTotal, MIN_ODDS, MAX_ODDS);

        this.team1HandicapBets = Generation.generateHandicapBets(team1, team1Score, team2Score, QUANTITY_OF_HANDICAPS, MIN_HANDICAP_VALUE,
                MAX_HANDICAP_VALUE, scoreDiff, MIN_ODDS, MAX_ODDS);

        this.team2HandicapBets = Generation.generateHandicapBets(team2, team2Score, team1Score, QUANTITY_OF_HANDICAPS, MIN_HANDICAP_VALUE,
                MAX_HANDICAP_VALUE, scoreDiff, MIN_ODDS, MAX_ODDS);

        this.quarterBets = Generation.generateQuarterBets(team1QuarterScores, team2QuarterScores, MIN_ODDS, MAX_ODDS);

        this.winningTeam = this.team1Score > this.team2Score ? team1 : team2;

        logger.info("Match between: {} vs {}, Winning team: {}", team1, team2, winningTeam);
    }

    public double processBet(String betType, int betIndex, String teamChoice, double betAmount, double userBalance) {
        logger.warn("Processing bet: Type={}, Team={}, Amount={}", betType, teamChoice, betAmount);
        double winAmount = 0;
        boolean isWin = false;

        switch (betType.toLowerCase()) {
            case "total":
                if (betIndex >= 0 && betIndex < totalBets.length) {
                    isWin = totalBets[betIndex].isWinTotal;
                    winAmount = isWin ? betAmount * totalBets[betIndex].odds : 0;
                    logger.info("Total bet result - Win: {}, Win Amount: {}", isWin, winAmount);
                }
                break;
            case "handicap":
                HandicapBet[] relevantBets = teamChoice.equals(team1) ? team1HandicapBets : team2HandicapBets;
                if (betIndex >= 0 && betIndex < relevantBets.length) {
                    isWin = relevantBets[betIndex].isWinHandicap;
                    winAmount = isWin ? betAmount * relevantBets[betIndex].getOdds() : 0;
                    logger.info("Handicap bet result - Team: {}, Win: {}, Win Amount: {}", teamChoice, isWin, winAmount);
                }
                break;
            case "quarter":
                if (betIndex >= 0 && betIndex < quarterBets.length) {
                    boolean relevantWin = teamChoice.equals(team1) ? quarterBets[betIndex].isTeam1Win() : quarterBets[betIndex].isTeam2Win();
                    double relevantOdds = teamChoice.equals(team1) ? quarterBets[betIndex].getTeam1Odds() : quarterBets[betIndex].getTeam2Odds();
                    isWin = relevantWin;
                    winAmount = isWin ? betAmount * relevantOdds : 0;
                    logger.info("Quarter bet result - Team: {}, Win: {}, Win Amount: {}", teamChoice, isWin, winAmount);
                }
                break;
            case "winlose":
                isWin = teamChoice.equals(winningTeam);
                double odds = teamChoice.equals(team1) ? team1Odds : team2Odds;
                winAmount = isWin ? betAmount * odds : 0;
                break;
            default:
                logger.error("Invalid bet type: {}", betType);
                throw new IllegalArgumentException("Invalid bet type");
        }

        return winAmount;
    }

    public boolean canPlaceBet(double userBalance, double betAmount) {
        return betAmount > 0 && userBalance >= betAmount;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Match: ").append(team1).append(" vs ").append(team2).append("\n");
        sb.append(String.format("Final Score: %s %d - %d %s\n", team1, team1Score, team2Score, team2));

        for (int i = 0; i < 4; i++) {
            sb.append(String.format("Quarter %d: %d - %d\n", i + 1, team1QuarterScores[i], team2QuarterScores[i]));
        }

        sb.append("\nWin/Lose: ").append(team1).append(" Odds: ").append(team1Odds)
                .append(team1.equals(winningTeam) ? " (W)" : " (L)").append(", ")
                .append(team2).append(" Odds: ").append(team2Odds)
                .append(team2.equals(winningTeam) ? " (W)" : " (L)").append("\n");

        sb.append("\nTotal Bets:\n").append(getTotalBetsInfo());
        sb.append("\nHandicap Bets for ").append(team1).append(":\n")
                .append(getHandicapBetsInfo(team1, team1HandicapBets));
        sb.append("\nHandicap Bets for ").append(team2).append(":\n")
                .append(getHandicapBetsInfo(team2, team2HandicapBets));
        sb.append("\nQuarter Bets:\n").append(getQuarterBetsInfo());

        return sb.toString();
    }

    private String getTotalBetsInfo() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < totalBets.length; i++) {
            sb.append(String.format("Total %d (+%d): %.2f (%s)\n",
                    i + 1, totalBets[i].totalValue, totalBets[i].odds,
                    totalBets[i].isWinTotal ? "W" : "L"));
        }
        return sb.toString();
    }

    private String getHandicapBetsInfo(String team, HandicapBet[] handicapBets) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < handicapBets.length; i++) {
            sb.append(String.format("Handicap Odds %d(+%d on %s win)(%s): %.2f\n",
                    i + 1,
                    handicapBets[i].getHandicapValue(),
                    team,
                    handicapBets[i].isWinHandicap ? "W" : "L",
                    handicapBets[i].getOdds()));
        }
        return sb.toString();
    }

    private String getQuarterBetsInfo() {
        StringBuilder sb = new StringBuilder();
        for (QuarterBet qBet : quarterBets) {
            sb.append(String.format("Quarter %d: %s %.2f (%s) - %s %.2f (%s)\n",
                    qBet.getQuarter(), team1, qBet.getTeam1Odds(), qBet.isTeam1Win() ? "W" : "L",
                    team2, qBet.getTeam2Odds(), qBet.isTeam2Win() ? "W" : "L"));
        }
        return sb.toString();
    }

}
