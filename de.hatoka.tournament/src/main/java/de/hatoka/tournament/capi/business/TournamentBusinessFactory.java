package de.hatoka.tournament.capi.business;

import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.RankPO;
import de.hatoka.tournament.capi.entities.TournamentPO;
import de.hatoka.tournament.internal.business.ITableBO;
import de.hatoka.tournament.internal.business.ITournamentBO;

public interface TournamentBusinessFactory
{
    CashGameCompetitorBO getCompetitorBO(CompetitorPO competitorPO, CashGameBO cashGameBO);

    CompetitorBO getCompetitorBO(CompetitorPO competitorPO, TournamentBO tournamentBO);

    PlayerBO getPlayerBO(PlayerPO playerPO);

    PlayerBORepository getPlayerBORepository(String accountRef);

    CashGameBO getCashGameBO(TournamentPO tournamentPO);

    TournamentBO getTournamentBO(TournamentPO tournamentPO);

    TournamentBORepository getTournamentBORepository(String accountRef);

    HistoryEntryBO getHistoryBO(HistoryPO historyPO);

    BlindLevelBO getBlindLevelBO(BlindLevelPO blindLevelPO, ITournamentBO tournament);

    PauseBO getPauseBO(BlindLevelPO blindLevelPO, ITournamentBO tournament);

    RankBO getRankBO(RankPO rankPO, TournamentBO tournamentBO);

    ITableBO getTableBO(int tableNo);
}
