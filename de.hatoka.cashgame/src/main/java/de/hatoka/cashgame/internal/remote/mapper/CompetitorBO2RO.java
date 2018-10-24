package de.hatoka.cashgame.internal.remote.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import de.hatoka.cashgame.capi.business.CompetitorBO;
import de.hatoka.cashgame.internal.remote.model.CompetitorRO;

@Component("CashGameCompetitorBO2RO")
public class CompetitorBO2RO implements Function<CompetitorBO, CompetitorRO>
{
    public CompetitorRO apply(CompetitorBO competitor)
    {
        CompetitorRO result = new CompetitorRO();
        result.setInPlay(competitor.getInPlay());
        result.setResult(competitor.getResult());
        result.setPosition(competitor.getPosition());
        result.setPlayerRef(competitor.getPlayer().getRef().toString());
        result.setPlayerName(competitor.getPlayer().getName());
        result.setActive(competitor.isActive());
        result.setStatus(competitor.getState());
        return result;
    }
}
