package de.hatoka.tournament.internal.business;

import java.util.List;
import java.util.stream.Collectors;

import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.capi.dao.PlayerDao;
import de.hatoka.tournament.capi.entities.PlayerPO;

public class PlayerBOImpl implements PlayerBO
{
    private PlayerPO playerPO;
    private final PlayerDao playerDao;
    private final TournamentBusinessFactory tournamentFactory;

    public PlayerBOImpl(PlayerPO playerPO, PlayerDao playerDao, TournamentBusinessFactory tournamentFactory)
    {
        this.playerPO = playerPO;
        this.playerDao = playerDao;
        this.tournamentFactory = tournamentFactory;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerPO == null) ? 0 : playerPO.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerBOImpl other = (PlayerBOImpl)obj;
        if (playerPO == null)
        {
            if (other.playerPO != null)
                return false;
        }
        else if (!playerPO.equals(other.playerPO))
            return false;
        return true;
    }

    @Override
    public String getID()
    {
        return playerPO.getId();
    }

    @Override
    public String getName()
    {
        return playerPO.getName();
    }

    @Override
    public void remove()
    {
        playerDao.remove(playerPO);
        playerPO = null;
    }

    @Override
    public String getEMail()
    {
        return playerPO.getEMail();
    }

    @Override
    public void seetEMail(String eMail)
    {
        playerPO.setEMail(eMail);
    }

    @Override
    public List<TournamentBO> getTournaments()
    {
        return playerPO.getCompetitors().stream().map(c -> c.getTournamentPO()).filter(t -> !t.isCashGame()).map(t -> tournamentFactory.getTournamentBO(t)).collect(Collectors.toList());
    }

    @Override
    public List<CashGameBO> getCashGames()
    {
        return playerPO.getCompetitors().stream().map(c -> c.getTournamentPO()).filter(t -> t.isCashGame()).map(t -> tournamentFactory.getCashGameBO(t)).collect(Collectors.toList());
    }

}
