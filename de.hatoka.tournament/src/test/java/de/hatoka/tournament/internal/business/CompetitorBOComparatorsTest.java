package de.hatoka.tournament.internal.business;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CompetitorBO;

public class CompetitorBOComparatorsTest
{
    @Mock
    private CompetitorBO competitorBO_A;
    @Mock
    private CompetitorBO competitorBO_B;
    @Mock
    private CompetitorBO competitorBO_C;
    @Mock
    private CompetitorBO competitorBO_D;

    @Before
    public void initMocks()
    {
        MockitoAnnotations.initMocks(this);
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
        competitorBOs.sort(CompetitorBOComparators.DEFAULT);
        assertEquals("D is first; inactive with most result", competitorBO_D, competitorBOs.get(0));
        assertEquals("C is second; inactive with lowest result", competitorBO_C, competitorBOs.get(1));
        assertEquals("B is third; active with lowest invest", competitorBO_B, competitorBOs.get(2));
        assertEquals("A is last; active with highest invest", competitorBO_A, competitorBOs.get(3));
    }

    @Test
    public void testSortingActive()
    {
        Mockito.when(competitorBO_A.isActive()).thenReturn(true);
        Mockito.when(competitorBO_C.isActive()).thenReturn(false);

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_A, competitorBO_C);
        competitorBOs.sort(CompetitorBOComparators.DEFAULT);
        assertEquals("C is second; inactive with lowest result", competitorBO_C, competitorBOs.get(0));
        assertEquals("A is last; active with highest invest", competitorBO_A, competitorBOs.get(1));
    }

    @Test
    public void testSortingActivePlayers()
    {
        Mockito.when(competitorBO_A.isActive()).thenReturn(true);
        Mockito.when(competitorBO_B.isActive()).thenReturn(true);
        Mockito.when(competitorBO_A.getInPlay()).thenReturn(Money.valueOf("15 EUR"));
        Mockito.when(competitorBO_B.getInPlay()).thenReturn(Money.valueOf("5 EUR"));

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_A, competitorBO_B);
        competitorBOs.sort(CompetitorBOComparators.DEFAULT);
        assertEquals("B is third; active with lowest invest", competitorBO_B, competitorBOs.get(0));
        assertEquals("A is last; active with highest invest", competitorBO_A, competitorBOs.get(1));
    }

    @Test
    public void testSortingInactivePlayers()
    {
        Mockito.when(competitorBO_C.isActive()).thenReturn(false);
        Mockito.when(competitorBO_D.isActive()).thenReturn(false);
        Mockito.when(competitorBO_C.getResult()).thenReturn(Money.valueOf("-5 EUR"));
        Mockito.when(competitorBO_D.getResult()).thenReturn(Money.valueOf("5 EUR"));

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_C, competitorBO_D);
        competitorBOs.sort(CompetitorBOComparators.DEFAULT);
        assertEquals("D is first; inactive with most result", competitorBO_D, competitorBOs.get(0));
        assertEquals("C is second; inactive with lowest result", competitorBO_C, competitorBOs.get(1));
    }

}
