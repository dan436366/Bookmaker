import org.example.Generation;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BettingAgencyMockitoTests {

    @Test
    void testGenerateQuarterScores() {
        Random mockRandom = Mockito.mock(Random.class);

        when(mockRandom.nextInt(11)).thenReturn(2, 3, 4, 5);

        Generation generation = new Generation();
        Generation.setRandom(mockRandom);

        int[] quarterScores = generation.generateQuarterScores(10, 20);

        assertEquals(12, quarterScores[0]);
        assertEquals(13, quarterScores[1]);
        assertEquals(14, quarterScores[2]);
        assertEquals(15, quarterScores[3]);

        verify(mockRandom, times(4)).nextInt(11);
    }

    @Test
    void testGenerateTotals() {
        Random mockRandom = Mockito.mock(Random.class);

        when(mockRandom.nextInt(10, 20)).thenReturn(15);
        when(mockRandom.nextInt(30, 40)).thenReturn(38);

        Generation.setRandom(mockRandom);

        int[] bounds = {10, 20, 30, 40};
        int[] totals = Generation.generateTotals(bounds);

        assertEquals(15, totals[0]);
        assertEquals(38, totals[1]);

        verify(mockRandom).nextInt(10, 20);
        verify(mockRandom).nextInt(30, 40);
    }

    @Test
    void testGenerateTeamOdds() {
        Random mockRandom = Mockito.mock(Random.class);

        when(mockRandom.nextDouble()).thenReturn(0.5);

        Generation generation = new Generation();
        Generation.setRandom(mockRandom);

        double odds = generation.generateTeamOdds(1.0, 2.0);

        assertEquals(1.50, odds);

        verify(mockRandom, times(1)).nextDouble();
    }
}
