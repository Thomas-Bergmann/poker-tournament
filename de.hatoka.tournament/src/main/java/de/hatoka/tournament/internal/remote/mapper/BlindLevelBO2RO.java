package de.hatoka.tournament.internal.remote.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.TournamentRoundBO;
import de.hatoka.tournament.internal.remote.model.BlindLevelRO;

@Component
public class BlindLevelBO2RO implements Function<TournamentRoundBO, BlindLevelRO>
{
    public BlindLevelRO apply(TournamentRoundBO round)
    {
        BlindLevelRO result = new BlindLevelRO();
        result.setPosition(round.getPosition());
        result.setDuration(round.getDuration());
        result.setEstStartDateTime(round.getStartTime());
        result.setActive(round.isActive());
        result.setRebuy(round.isRebuyAllowed());

        BlindLevelBO blindLevelBO = round.getBlindLevel();
        if (blindLevelBO == null)
        {
            result.setPause(true);
        }
        else
        {
            result.setSmallBlind(blindLevelBO.getSmallBlind());
            result.setBigBlind(blindLevelBO.getBigBlind());
            result.setAnte(blindLevelBO.getAnte());
        }
        return result;
    }
}
