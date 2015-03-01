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
        Mockito.when(competitorBO_A.getInPlay()).thenReturn(Money.getInstance("-15 EUR"));
        Mockito.when(competitorBO_B.getInPlay()).thenReturn(Money.getInstance("-5 EUR"));
        Mockito.when(competitorBO_C.getInPlay()).thenReturn(Money.getInstance("-5 EUR"));
        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_A, competitorBO_B, competitorBO_C);
        competitorBOs.sort(CompetitorBOComparators.DEFAULT);
        assertEquals("first is inactive", competitorBO_C, competitorBOs.get(0));
        assertEquals("second is active and has lowest invest", competitorBO_B, competitorBOs.get(1));
        assertEquals("third is active and has highest invest", competitorBO_A, competitorBOs.get(2));
    }

}
