package de.hatoka.tournament.capi.business;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.internal.business.TournamentBOImpl;

public class TournamentBOTest
{
    private static TournamentBO UNDER_TEST;

    @Mock
    private TournamentBusinessFactory factory;
    @Mock
    private PlayerBO playerBO;
    @Mock
    private TournamentPO tournamentPO;
    @Mock
    private TournamentDao tournamentDao;
    @Mock
    private CompetitorDao competitorDao;
    @Mock
    private PlayerDao playerDao;
    @Mock
    private PlayerPO playerPO;
    @Mock
    private CompetitorPO competitor1;
    @Mock
    private CompetitorPO competitor2;
    @Mock
    private CompetitorBO competitorBO1;
    @Mock
    private CompetitorBO competitorBO2;

    @Before
    public void createTestObject()
    {
        MockitoAnnotations.initMocks(this);
        UNDER_TEST = new TournamentBOImpl(tournamentPO, tournamentDao, competitorDao, playerDao, factory);

    }

    @Test
    public void testDeletion()
    {
        Set<CompetitorPO> listOfCompetitorPOs = new HashSet<CompetitorPO>();
        listOfCompetitorPOs.add(competitor1);
        listOfCompetitorPOs.add(competitor2);
        Mockito.when(tournamentPO.getCompetitors()).thenReturn(listOfCompetitorPOs);
        Mockito.when(factory.getCompetitorBO(competitor1)).thenReturn(competitorBO1);
        Mockito.when(factory.getCompetitorBO(competitor2)).thenReturn(competitorBO2);
        whenRemoveBORemovePOFromSet(competitorBO1, competitor1, listOfCompetitorPOs);
        whenRemoveBORemovePOFromSet(competitorBO2, competitor2, listOfCompetitorPOs);
        UNDER_TEST.remove();
    }

    private void whenRemoveBORemovePOFromSet(CompetitorBO competitorBO, CompetitorPO competitorPO,
                    Set<CompetitorPO> listOfCompetitorPOs)
    {
        Mockito.when(competitorBO.remove()).then(new Answer<String>()
        {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable
            {
                listOfCompetitorPOs.remove(competitorPO);
                return "1";
            }
        });
    }

}
