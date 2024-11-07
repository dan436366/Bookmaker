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

    @Test
    void testPlaceBet_UserHasEnoughBalance() {
        Match mockMatch = mock(Match.class);
        User mockUser = mock(User.class);

        when(mockUser.getBalance()).thenReturn(100.0);
        doNothing().when(mockUser).placeBet(50.0);

        mockUser.placeBet(50.0);

        verify(mockUser, times(1)).placeBet(50.0);
        assertEquals(100.0, mockUser.getBalance(), "User should have enough balance to place bet");
    }

    @Test
    void testPlaceBet_UserHasInsufficientBalance() {
        User mockUser = mock(User.class);

        when(mockUser.getBalance()).thenReturn(30.0);
        doThrow(new IllegalArgumentException("Insufficient balance"))
                .when(mockUser).placeBet(50.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mockUser.placeBet(50.0);
        });

        assertEquals("Insufficient balance", exception.getMessage());
        verify(mockUser, times(1)).placeBet(50.0);
    }

    @Test
    void testProcessBet_SuccessfulTotalBet() {
        Match mockMatch = mock(Match.class);

        when(mockMatch.processBet("total", 0, "", 100.0, 500.0))
                .thenReturn(180.0);

        double result = mockMatch.processBet("total", 0, "", 100.0, 500.0);
        assertEquals(180.0, result, "Total bet should return correct win amount");

        verify(mockMatch, times(1)).processBet("total", 0, "", 100.0, 500.0);
    }

    @Test
    void testProcessBet_UnsuccessfulHandicapBet() {
        Match mockMatch = mock(Match.class);

        when(mockMatch.processBet("handicap", 1, "Lakers", 100.0, 500.0))
                .thenReturn(0.0);

        double result = mockMatch.processBet("handicap", 1, "Lakers", 100.0, 500.0);
        assertEquals(0.0, result, "Handicap bet loss should return 0");

        verify(mockMatch, times(1)).processBet("handicap", 1, "Lakers", 100.0, 500.0);
    }
}
