package de.hatoka.tournament.internal.remote.mapper;

import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.RankBO;
import de.hatoka.tournament.internal.remote.model.RankRO;

@Component
public class RankBO2RO
{
    public RankRO apply(RankBO rank)
    {
        RankRO result = new RankRO();
        result.setFirstPosition(rank.getFirstPosition());
        result.setLastPosition(rank.getLastPosition());
        result.setPercentage(rank.getPercentage());
        result.setAmountPerPlayer(rank.getAmountPerPlayer());
        result.setAmount(rank.getAmount());
        return result;
    }
}
