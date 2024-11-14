import org.example.Match;
import org.example.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BettingAgencyTests{

    @Test
    void testPlaceBet() {
        User user = new User("TestUser", 100);
        user.placeBet(50);
        assertEquals(50, user.getBalance(), "Balance should decrease after placing a bet");
        System.out.println("Test for placing a bet passed");
    }

    @Test
    void testWinBet() {
        User user = new User("TestUser", 50);
        user.winBet(30);
        assertEquals(80, user.getBalance(), "Balance should increase after winning a bet");
        System.out.println("Test for winning a bet passed");
    }

    @Test
    void testCanPlaceBet() {
        Match match = new Match("Lakers", "Warriors");
        assertTrue(match.canPlaceBet(100.0, 50.0), "User should be able to place the bet with sufficient balance");
        System.out.println("Test for 'can place bet' a bet passed");
    }

    @Test
    void testCantPlaceBet() {
        Match match = new Match("Lakers", "Nets");
        assertFalse(match.canPlaceBet(100.0, 101.0), "User should not be able to place the bet with insufficient balance");
        System.out.println("Test for 'can't place bet' a bet passed");
    }

//    @Test
//    void testProcessBet_TotalBet_Win() {
//        Match match = new Match("Lakers", "Warriors");
//        double betAmount = 100.0;
//        int betIndex = 0;
//        match.totalBets[betIndex] = new Match.TotalBet(match.team1Score + match.team2Score - 1, 1.8, true);
//        double winAmount = match.processBet("total", betIndex, "", betAmount, 500);
//
//        assertEquals(betAmount * 1.8, winAmount, "Total Bet win amount should be correct on win.");
//        System.out.println("testProcessBet_TotalBet_Win: Test passed successfully.");
//    }
//
//    @Test
//    void testProcessBet_TotalBet_Lose() {
//        Match match = new Match("Lakers", "Warriors");
//        double betAmount = 100.0;
//        int betIndex = 0;
//        match.totalBets[betIndex] = new Match.TotalBet(match.team1Score + match.team2Score + 1, 1.8, false);
//        double winAmount = match.processBet("total", betIndex, "", betAmount, 500);
//
//        assertEquals(0.0, winAmount, "Total Bet amount should be 0 on loss.");
//        System.out.println("testProcessBet_TotalBet_Lose: Test passed successfully.");
//    }
//
//    @Test
//    void testProcessBet_HandicapBet_Team1_Win() {
//        Match match = new Match("Lakers", "Warriors");
//        double betAmount = 100.0;
//        int betIndex = 0;
//        match.team1HandicapBets[betIndex] = new Match.HandicapBet(5, 2.0, true);
//        double winAmount = match.processBet("handicap", betIndex, match.getTeam1(), betAmount, 500);
//
//        assertEquals(betAmount * 2.0, winAmount, "Handicap Bet win amount should be correct when Team 1 wins.");
//        System.out.println("testProcessBet_HandicapBet_Team1_Win: Test passed successfully.");
//    }
//
//    @Test
//    void testProcessBet_HandicapBet_Team2_Lose() {
//        Match match = new Match("Lakers", "Warriors");
//        double betAmount = 100.0;
//        int betIndex = 0;
//        match.team2HandicapBets[betIndex] = new Match.HandicapBet(5, 2.5, false);
//        double winAmount = match.processBet("handicap", betIndex, match.getTeam2(), betAmount, 500);
//
//        assertEquals(0.0, winAmount, "Handicap Bet amount should be 0 on loss for Team 2.");
//        System.out.println("testProcessBet_HandicapBet_Team2_Lose: Test passed successfully.");
//    }
//
//    @Test
//    void testProcessBet_QuarterBet_Team1_Win() {
//        Match match = new Match("Lakers", "Warriors");
//        double betAmount = 100.0;
//        int quarterIndex = 0;
//        match.quarterBets[quarterIndex] = new Match.QuarterBet(1, 1.8, 2.0, true, false);
//        double winAmount = match.processBet("quarter", quarterIndex, match.getTeam1(), betAmount, 500);
//
//        assertEquals(betAmount * 1.9, winAmount, "Quarter Bet win amount should be correct when Team 1 wins.");
//        System.out.println("testProcessBet_QuarterBet_Team1_Win: Test passed successfully.");
//    }
//
//    @Test
//    void testProcessBet_QuarterBet_Team2_Lose() {
//        Match match = new Match("Lakers", "Warriors");
//        double betAmount = 100.0;
//        int quarterIndex = 0;
//        match.quarterBets[quarterIndex] = new Match.QuarterBet(1, 1.8, 2.0, false, true);
//        double winAmount = match.processBet("quarter", quarterIndex, match.getTeam1(), betAmount, 500);
//
//        assertEquals(0.0, winAmount, "Quarter Bet amount should be 0 on loss for Team 1.");
//        System.out.println("testProcessBet_QuarterBet_Team2_Lose: Test passed successfully.");
//    }

    @Test
    void testProcessBet_WinLose_Team1_Win() {
        Match match = new Match("Lakers", "Warriors");
        double betAmount = 100.0;
        match.winningTeam = match.getTeam1();
        match.team1Odds = 1.5;
        double winAmount = match.processBet("winlose", -1, match.getTeam1(), betAmount, 500);

        assertEquals(betAmount * 1.5, winAmount, "WinLose Bet win amount should be correct when Team 1 wins.");
        System.out.println("testProcessBet_WinLose_Team1_Win: Test passed successfully.");
    }

    @Test
    void testProcessBet_WinLose_Team2_Lose() {
        Match match = new Match("Lakers", "Warriors");
        double betAmount = 100.0;
        match.winningTeam = match.getTeam1();
        match.team2Odds = 2.0;
        double winAmount = match.processBet("winlose", -1, match.getTeam2(), betAmount, 500);

        assertEquals(0.0, winAmount, "WinLose Bet amount should be 0 on loss for Team 2.");
        System.out.println("testProcessBet_WinLose_Team2_Lose: Test passed successfully.");
    }

//    @Test
//    public void testCalculateTotalScores() {
//        Match match = new Match("Lakers", "Warriors");
//
//        match.team1QuarterScores = new int[] {20, 25, 15, 30};
//        match.team2QuarterScores = new int[] {18, 22, 20, 25};
//
//        match.calculateTotalScores();
//
//        assertEquals(90, match.team1Score, "Total score team 1 false");
//        assertEquals(85, match.team2Score, "Total score team 2 false");
//
//        System.out.println("testCalculateTotalScores: Test passed successfully.");
//    }
}

