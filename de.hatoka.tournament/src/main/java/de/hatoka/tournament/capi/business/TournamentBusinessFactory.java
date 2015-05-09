package de.hatoka.tournament.capi.business;

import de.hatoka.tournament.capi.entities.BlindLevelPO;
import de.hatoka.tournament.capi.entities.CompetitorPO;
import de.hatoka.tournament.capi.entities.HistoryPO;
import de.hatoka.tournament.capi.entities.PlayerPO;
import de.hatoka.tournament.capi.entities.TournamentPO;

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

    BlindLevelBO getBlindLevelBO(BlindLevelPO blindLevelPO);

    TournamentRoundBO getPauseBO(BlindLevelPO blindLevelPO);

}
