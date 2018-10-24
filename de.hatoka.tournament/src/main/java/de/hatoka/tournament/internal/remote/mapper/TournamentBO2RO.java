package de.hatoka.tournament.internal.remote.mapper;

import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.internal.remote.model.TournamentRO;

@Component
public class TournamentBO2RO
{
    public TournamentRO apply(TournamentBO tournamentBO)
    {
        TournamentRO result = new TournamentRO();
        result.setBuyIn(tournamentBO.getBuyIn());
        result.setSumInPlay(tournamentBO.getSumInplay());
        result.setCompetitorsSize(tournamentBO.getCompetitors().size());
        result.setName(tournamentBO.getName());
        result.setDate(tournamentBO.getStartTime());
        result.setInitialStack(tournamentBO.getInitialStacksize());
        result.setLargestTable(tournamentBO.getMaximumNumberOfPlayersPerTable());
        result.setReBuy(tournamentBO.getReBuy());
        return result;
    }
}
