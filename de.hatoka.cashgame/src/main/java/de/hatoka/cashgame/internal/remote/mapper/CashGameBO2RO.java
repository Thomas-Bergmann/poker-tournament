package de.hatoka.cashgame.internal.remote.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.internal.remote.model.CashGameDataRO;
import de.hatoka.cashgame.internal.remote.model.CashGameInfoRO;
import de.hatoka.cashgame.internal.remote.model.CashGameRO;

@Component
public class CashGameBO2RO
{
    public CashGameRO apply(CashGameBO cashGameBO)
    {
        CashGameRO result = new CashGameRO();
        CashGameDataRO data = new CashGameDataRO();
        data.setBuyIn(cashGameBO.getBuyIn());
        data.setDate(cashGameBO.getDate());
        CashGameInfoRO info = new CashGameInfoRO();
        info.setSumInPlay(cashGameBO.getSumInplay());
        result.setData(data);
        result.setInfo(info);
        result.setRef(cashGameBO.getRef().getGlobalRef());
        return result;
    }

    public List<CashGameRO> apply(List<CashGameBO> cashGames)
    {
        return cashGames.stream().map(this::apply).collect(Collectors.toList());
    }
}
