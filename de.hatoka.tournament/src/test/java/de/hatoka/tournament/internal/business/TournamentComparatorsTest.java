package de.hatoka.tournament.internal.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hatoka.common.capi.math.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.TournamentComparators;

public class TournamentComparatorsTest
{
    @Mock
    protected CompetitorBO competitorBO_A;
    @Mock
    protected CompetitorBO competitorBO_B;
    @Mock
    protected CompetitorBO competitorBO_C;
    @Mock
    protected CompetitorBO competitorBO_D;
    private AutoCloseable mocks = null;

    @BeforeEach
    public void initMocks()
    {
        mocks  = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void closeMocks() throws Exception
    {
        if (mocks != null)
        {
            mocks.close();
        }
    }

    @Test
    public void testDefaultSorting()
    {
        Mockito.when(competitorBO_A.isActive()).thenReturn(true);
        Mockito.when(competitorBO_B.isActive()).thenReturn(true);
        Mockito.when(competitorBO_C.isActive()).thenReturn(false);
        Mockito.when(competitorBO_D.isActive()).thenReturn(false);
        Mockito.when(competitorBO_A.getInPlay()).thenReturn(Money.valueOf("15 EUR"));
        Mockito.when(competitorBO_B.getInPlay()).thenReturn(Money.valueOf("5 EUR"));
        Mockito.when(competitorBO_C.getResult()).thenReturn(Money.valueOf("-5 EUR"));
        Mockito.when(competitorBO_D.getResult()).thenReturn(Money.valueOf("5 EUR"));

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_A, competitorBO_B, competitorBO_C, competitorBO_D);
        competitorBOs.sort(TournamentComparators.COMPETITOR);
        assertEquals(competitorBO_B, competitorBOs.get(0), "B is first; active with lowest invest");
        assertEquals(competitorBO_A, competitorBOs.get(1), "A is second; active with highest invest");
        assertEquals(competitorBO_D, competitorBOs.get(2), "D is third; inactive with most result");
        assertEquals(competitorBO_C, competitorBOs.get(3), "C is last; inactive with lowest result");
    }

    @Test
    public void testSortingActive()
    {
        Mockito.when(competitorBO_A.isActive()).thenReturn(true);
        Mockito.when(competitorBO_C.isActive()).thenReturn(false);

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_A, competitorBO_C);
        competitorBOs.sort(TournamentComparators.COMPETITOR);
        assertEquals(competitorBO_A, competitorBOs.get(0), "A is first; active with highest invest");
        assertEquals(competitorBO_C, competitorBOs.get(1), "C is second; inactive with lowest result");
    }

    @Test
    public void testSortingActivePlayers()
    {
        Mockito.when(competitorBO_A.isActive()).thenReturn(true);
        Mockito.when(competitorBO_B.isActive()).thenReturn(true);
        Mockito.when(competitorBO_A.getInPlay()).thenReturn(Money.valueOf("15 EUR"));
        Mockito.when(competitorBO_B.getInPlay()).thenReturn(Money.valueOf("5 EUR"));

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_A, competitorBO_B);
        competitorBOs.sort(TournamentComparators.COMPETITOR);
        assertEquals(competitorBO_B, competitorBOs.get(0), "B is third; active with lowest invest");
        assertEquals(competitorBO_A, competitorBOs.get(1), "A is last; active with highest invest");
    }

    @Test
    public void testSortingInactivePlayers()
    {
        Mockito.when(competitorBO_C.isActive()).thenReturn(false);
        Mockito.when(competitorBO_D.isActive()).thenReturn(false);
        Mockito.when(competitorBO_C.getResult()).thenReturn(Money.valueOf("-5 EUR"));
        Mockito.when(competitorBO_D.getResult()).thenReturn(Money.valueOf("5 EUR"));

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_C, competitorBO_D);
        competitorBOs.sort(TournamentComparators.COMPETITOR);
        assertEquals(competitorBO_D, competitorBOs.get(0), "D is first; inactive with most result");
        assertEquals(competitorBO_C, competitorBOs.get(1), "C is second; inactive with lowest result");
    }

}
