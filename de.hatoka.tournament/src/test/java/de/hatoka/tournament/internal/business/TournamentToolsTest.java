package de.hatoka.tournament.internal.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tests.de.hatoka.tournament.TournamentTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TournamentTestConfiguration.class })
public class TournamentToolsTest
{
    @Autowired
    private TournamentTools underTest;

    @Test
    public void testNextSmallBlind()
    {
        assertEquals(20, underTest.getNextSmallBlind(10));
        assertEquals(200, underTest.getNextSmallBlind(100));
        assertEquals(500, underTest.getNextSmallBlind(200));
        assertEquals(1000, underTest.getNextSmallBlind(500));
        assertEquals(2000, underTest.getNextSmallBlind(1000));
    }

    @Test
    public void testNextBigBlind()
    {
        assertEquals(20, underTest.getBigBlind(10));
    }
}
