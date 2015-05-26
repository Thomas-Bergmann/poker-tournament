package de.hatoka.tournament.internal.templates.app;

import java.math.BigDecimal;
import java.util.Date;

import javax.ws.rs.core.UriBuilder;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.internal.app.models.BlindLevelVO;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.PlayerVO;
import de.hatoka.tournament.internal.app.models.RankVO;
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
        result.setSumInPlay(new MoneyVO(Money.valueOf("200", "USD")));
        result.setCompetitorsSize(20);
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

    /* package */static BlindLevelVO getBlindLevelVO(String id, int small, int big, int ante, int duration)
    {
        BlindLevelVO result = getBlindLevelVO(small, big, ante, duration);
        result.setId(id);
        return result;
    }

    /* package */static BlindLevelVO getBlindLevelVO(int small, int big, int ante, int duration)
    {
        BlindLevelVO result = new BlindLevelVO();
        result.setPause(false);
        result.setSmallBlind(small);
        result.setBigBlind(big);
        result.setAnte(ante);
        result.setDuration(duration);
        return result;
    }

    /* package */static BlindLevelVO getPauseVO(String id, int duration)
    {
        BlindLevelVO result = new BlindLevelVO();
        result.setId(id);
        result.setPause(true);
        result.setDuration(duration);
        return result;
    }

    /* package */static RankVO getFixPriceRankVO(String id, int firstPosition, int lastPosition,
                    BigDecimal amountPerPlayer)
    {
        RankVO result = new RankVO();
        result.setId(id);
        result.setFirstPosition(firstPosition);
        result.setLastPosition(lastPosition);
        result.setAmountPerPlayer(amountPerPlayer);
        result.setAmountPerPlayerCalculated(false);
        result.setAmount(amountPerPlayer.multiply(BigDecimal.valueOf(lastPosition - firstPosition + 1)));
        return result;
    }

    public static RankVO getPercentageRankVO(String id, int firstPosition, int lastPosition, BigDecimal percentage, BigDecimal amount)
    {
        RankVO result = new RankVO();
        result.setId(id);
        result.setFirstPosition(firstPosition);
        result.setLastPosition(lastPosition);
        result.setPercentage(percentage);
        result.setPercentageCalculated(false);
        result.setAmountPerPlayer(amount.divide(BigDecimal.valueOf(lastPosition - firstPosition + 1), 2, BigDecimal.ROUND_DOWN).stripTrailingZeros());
        result.setAmountPerPlayerCalculated(true);
        result.setAmount(amount);
        return result;
    }

    public static RankVO getPercentageCalcRankVO(String id, int firstPosition, int lastPosition, BigDecimal percentageCalculated,
                    BigDecimal amount)
    {
        RankVO result = getPercentageRankVO(id, firstPosition, lastPosition, percentageCalculated, amount);
        result.setPercentageCalculated(true);
        return result;
    }

}
