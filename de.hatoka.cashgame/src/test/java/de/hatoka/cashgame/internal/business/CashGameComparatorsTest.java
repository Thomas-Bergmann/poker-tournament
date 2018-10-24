package de.hatoka.cashgame.internal.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import de.hatoka.cashgame.capi.business.CompetitorBO;
import de.hatoka.common.capi.math.Money;

public class CashGameComparatorsTest
{
    @Mock
    private CompetitorBO competitorBO_A;
    @Mock
    private CompetitorBO competitorBO_B;
    @Mock
    private CompetitorBO competitorBO_C;
    @Mock
    private CompetitorBO competitorBO_D;
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
        Mockito.when(competitorBO_A.getResult()).thenReturn(Money.valueOf("15 EUR"));
        Mockito.when(competitorBO_B.getResult()).thenReturn(Money.valueOf("5 EUR"));
        Mockito.when(competitorBO_C.getResult()).thenReturn(Money.valueOf("-5 EUR"));
        Mockito.when(competitorBO_D.getResult()).thenReturn(Money.valueOf("5 EUR"));

        List<CompetitorBO> competitorBOs = Arrays.asList(competitorBO_A, competitorBO_B, competitorBO_C, competitorBO_D);
        competitorBOs.sort(CashGameComparators.RESULT);
        assertEquals(competitorBO_A, competitorBOs.get(0), "highest result");
        assertEquals(competitorBO_B, competitorBOs.get(1), "same result as D but active");
        assertEquals(competitorBO_D, competitorBOs.get(2), "same result as B but inactive");
        assertEquals(competitorBO_C, competitorBOs.get(3), "lowest result");
    }
}
