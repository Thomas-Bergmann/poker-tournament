package de.hatoka.tournament.internal.business;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TournamentToolsTest
{
    @Test
    public void testNextSmallBlind()
    {
        assertEquals(20, TournamentTools.getNextSmallBlind(10));
        assertEquals(200, TournamentTools.getNextSmallBlind(100));
        assertEquals(500, TournamentTools.getNextSmallBlind(200));
        assertEquals(1000, TournamentTools.getNextSmallBlind(500));
        assertEquals(2000, TournamentTools.getNextSmallBlind(1000));
    }
    @Test
    public void testNextBigBlind()
    {
        assertEquals(20, TournamentTools.getBigBlind(10));
    }
}
