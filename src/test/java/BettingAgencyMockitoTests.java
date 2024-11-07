import org.example.Match;
import org.example.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class BettingAgencyMockitoTests {

    @Mock
    private User mockUser;

    @Mock
    private Match mockMatch;

    private BettingAgencyMockitoTests () {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessBet_TotalBet_Win() {
        double betAmount = 100.0;
        int betIndex = 0;
        when(mockMatch.processBet("total", betIndex, "", betAmount, 500)).thenReturn(betAmount * 1.8);

        double winAmount = mockMatch.processBet("total", betIndex, "", betAmount, 500);
        assertEquals(betAmount * 1.8, winAmount, "Total Bet win amount should be correct on win.");
    }

    @Test
    void testProcessBet_TotalBet_Lose() {
        double betAmount = 100.0;
        int betIndex = 0;
        when(mockMatch.processBet("total", betIndex, "", betAmount, 500)).thenReturn(0.0);

        double winAmount = mockMatch.processBet("total", betIndex, "", betAmount, 500);
        assertEquals(0.0, winAmount, "Total Bet amount should be 0 on loss.");
    }

    @Test
    void testProcessBet_HandicapBet_Team1_Win() {
        double betAmount = 100.0;
        int betIndex = 0;
        when(mockMatch.processBet("handicap", betIndex, mockMatch.getTeam1(), betAmount, 500)).thenReturn(betAmount * 2.0);

        double winAmount = mockMatch.processBet("handicap", betIndex, mockMatch.getTeam1(), betAmount, 500);
        assertEquals(betAmount * 2.0, winAmount, "Handicap Bet win amount should be correct when Team 1 wins.");
    }

    @Test
    void testProcessBet_HandicapBet_Team2_Lose() {
        double betAmount = 100.0;
        int betIndex = 0;
        when(mockMatch.processBet("handicap", betIndex, mockMatch.getTeam2(), betAmount, 500)).thenReturn(0.0);

        double winAmount = mockMatch.processBet("handicap", betIndex, mockMatch.getTeam2(), betAmount, 500);
        assertEquals(0.0, winAmount, "Handicap Bet amount should be 0 on loss for Team 2.");
    }
}
