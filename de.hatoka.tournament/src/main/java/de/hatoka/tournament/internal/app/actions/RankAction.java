package de.hatoka.tournament.internal.app.actions;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.RankBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.RankVO;
import de.hatoka.tournament.internal.app.models.TournamentRankModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class RankAction
{
    private final TournamentBusinessFactory factory;
    private final String accountRef;
    private final TournamentBO tournamentBO;

    public RankAction(String accountRef, String tournamentID, TournamentBusinessFactory factory)
    {
        this.accountRef = accountRef;
        this.factory = factory;
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        this.tournamentBO = tournamentBORepository.getTournamentByID(tournamentID);
    }

    public RankBO createRank(String firstPositionString, String lastPositionString, String amountString, String percentageString)
    {
        if (firstPositionString == null || firstPositionString.trim().isEmpty())
        {
            return null;
        }
        Integer firstPosition = Integer.valueOf(firstPositionString);
        Integer lastPosition = Integer.valueOf(lastPositionString);
        if (amountString !=null && !amountString.trim().isEmpty())
        {
            BigDecimal amount = new BigDecimal(amountString);
            return tournamentBO.createFixRank(firstPosition, lastPosition, amount);
        }
        BigDecimal percentage = new BigDecimal(percentageString);
        return tournamentBO.createRank(firstPosition, lastPosition, percentage);
    }


    public void deleteRanks(List<String> identifiers)
    {
        Iterator<RankBO> itRanks = tournamentBO.getRanks().iterator();
        while(itRanks.hasNext())
        {
            RankBO rank = itRanks.next();
            if (identifiers.contains(rank.getID()))
            {
                tournamentBO.remove(rank);
            }
        }
        if (tournamentBO.getRanks().isEmpty())
        {
            tournamentBO.createRank(1, 1, new BigDecimal("0.5"));
            tournamentBO.createRank(2, 2, new BigDecimal("0.3"));
            tournamentBO.createRank(3, 3, new BigDecimal("0.2"));
        }
    }

    public PlayerBO getPlayer(String playerId)
    {
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        return playerBORepository.findByID(playerId);
    }

    public TournamentRankModel getTournamentRankModel(URI tournamentURI)
    {
        TournamentRankModel model = new TournamentRankModel();
        model.setTournament(new TournamentVO(tournamentBO, tournamentURI));
        List<RankVO> ranks = new ArrayList<>();
        for(RankBO rank : tournamentBO.getRanks())
        {
            ranks.add(new RankVO(rank));
        }
        model.setRanks(ranks);
        return model;
    }
}
