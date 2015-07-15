package de.hatoka.tournament.internal.business;

import java.util.Date;

import javax.inject.Inject;

import com.google.inject.Provider;

import de.hatoka.common.capi.dao.SequenceProvider;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.CashGameCompetitorBO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.HistoryEntryBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.RankBO;
import de.hatoka.tournament.capi.business.TableBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.capi.dao.BlindLevelDao;
import de.hatoka.tournament.capi.dao.CompetitorDao;
import de.hatoka.tournament.capi.dao.HistoryDao;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.dao.RankDao;
import de.hatoka.tournament.capi.dao.TournamentDao;
import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.RankPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

public class TournamentBusinessFactoryImpl implements TournamentBusinessFactory
{
    @Inject
    private TournamentDao tournamentDao;

    @Inject
    private PlayerDao playerDao;

    @Inject
    private CompetitorDao competitorDao;

    @Inject
    private HistoryDao historyDao;

    @Inject
    private BlindLevelDao blindLevelDao;

    @Inject
    private RankDao rankDao;

    @Inject
    private Provider<Date> dateProvider;

    @Inject
    private UUIDGenerator uuidGenerator;

    @Inject
    private SequenceProvider sequenceProvider;

    @Inject
    private TransactionProvider transactionProvider;

    @Override
    public CashGameCompetitorBO getCompetitorBO(CompetitorPO competitorPO, CashGameBO cashGameBO)
    {
        return new CompetitorBOImpl(competitorPO, cashGameBO, historyDao, dateProvider, this);
    }

    @Override
    public CompetitorBO getCompetitorBO(CompetitorPO competitorPO, TournamentBO tournamentBO)
    {
        return new CompetitorBOImpl(competitorPO, tournamentBO, historyDao, dateProvider, this);
    }

    @Override
    public PlayerBO getPlayerBO(PlayerPO playerPO)
    {
        return new PlayerBOImpl(playerPO, playerDao);
    }

    @Override
    public PlayerBORepository getPlayerBORepository(String accountRef)
    {
        return new PlayerBORepositoryImpl(accountRef, playerDao, sequenceProvider.create(accountRef), this);
    }

    @Override
    public CashGameBO getCashGameBO(TournamentPO tournamentPO)
    {
        return new CashGameBOImpl(tournamentPO, tournamentDao, competitorDao, playerDao, this);
    }

    @Override
    public TournamentBO getTournamentBO(TournamentPO tournamentPO)
    {
        return new TournamentBOImpl(tournamentPO, tournamentDao, competitorDao, playerDao, blindLevelDao, rankDao, this);
    }

    @Override
    public TournamentBORepository getTournamentBORepository(String accountRef)
    {
        return new TournamentBORepositoryImpl(accountRef, tournamentDao, playerDao, competitorDao, blindLevelDao, historyDao, rankDao, sequenceProvider.create(accountRef), uuidGenerator, transactionProvider, this);
    }

    @Override
    public HistoryEntryBO getHistoryBO(HistoryPO historyPO)
    {
        return new HistoryEntryBOImpl(historyPO, this);
    }

    @Override
    public BlindLevelBO getBlindLevelBO(BlindLevelPO blindLevelPO)
    {
        return new BlindLevelBOImpl(blindLevelPO);
    }

    @Override
    public TournamentRoundBO getPauseBO(BlindLevelPO blindLevelPO)
    {
        return new BlindLevelBOImpl(blindLevelPO);
    }

    @Override
    public RankBO getRankBO(RankPO rankPO, TournamentBO tournamentBO)
    {
        return new RankBOImpl(rankPO, tournamentBO);
    }

    @Override
    public TableBO getTableBO(int tableNo)
    {
        return new TableBOImpl(tableNo);
    }
}
