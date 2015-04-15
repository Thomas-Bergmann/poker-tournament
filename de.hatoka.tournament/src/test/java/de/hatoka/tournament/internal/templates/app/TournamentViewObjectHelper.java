package de.hatoka.tournament.internal.templates.app;

import java.util.Date;

import javax.ws.rs.core.UriBuilder;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.PlayerVO;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public final class TournamentViewObjectHelper
{
    private TournamentViewObjectHelper()
    {
    }

    /* package */static TournamentVO getTournamentVO(String id, String name, Date date)
    {
        TournamentVO result = new TournamentVO();
        result.setId(id);
        result.setName(name);
        result.setDate(date);
        result.setBuyIn(new MoneyVO(Money.ONE_USD));
        result.setUri(UriBuilder.fromPath("tournament/{id}/players.html").build(id));
        result.setAverage(new MoneyVO(Money.ONE_USD));
        result.setSumInPlay(new MoneyVO(Money.getInstance("2", "USD")));
        result.setCompetitorsSize(10);
        return result;
    }

    /* package */static PlayerVO getPlayerVO(String id, String name)
    {
        PlayerVO result = new PlayerVO();
        result.setId(id);
        result.setName(name);
        return result;
    }

    /* package */static CompetitorVO getCompetitorVO(String id, String name, String playerID)
    {
        CompetitorVO result = new CompetitorVO();
        result.setId(id);
        result.setPlayerName(name);
        result.setPlayerId(playerID);
        result.setInPlay(new MoneyVO(Money.ONE_USD));
        result.setResult(new MoneyVO(Money.NOTHING));
        result.setActive(true);
        return result;
    }

}
