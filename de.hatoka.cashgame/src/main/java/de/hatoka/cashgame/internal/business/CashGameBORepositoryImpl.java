package de.hatoka.cashgame.internal.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.capi.business.CashGameBORepository;
import de.hatoka.cashgame.capi.business.CashGameRef;
import de.hatoka.cashgame.internal.persistence.CashGameDao;
import de.hatoka.cashgame.internal.persistence.CashGamePO;
import de.hatoka.user.capi.business.UserRef;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CashGameBORepositoryImpl implements CashGameBORepository
{
    @Autowired
    private CashGameBOFactory boFactory;
    @Autowired
    private CashGameDao cashgameDao;

    @Override
    public CashGameBO createCashGame(UserRef userRef, String localRef, Date date)
    {
        String name = new SimpleDateFormat("yyyy/mm/dd hh:MM").format(date);
        return boFactory.get(createTournamentPO(userRef, localRef, name, date));
    }

    private CashGamePO createTournamentPO(UserRef userRef, String localRef, String name, Date date)
    {
        CashGamePO tournamentPO = new CashGamePO();
        tournamentPO.setOwnerRef(userRef.getGlobalRef());
        tournamentPO.setLocalRef(localRef);
        tournamentPO.setStartDate(date);
        return cashgameDao.save(tournamentPO);
    }

    @Override
    public List<CashGameBO> getCashGames(UserRef userRef)
    {
        return cashgameDao.getByOwnerRef(userRef.getGlobalRef()).stream()
                        .map(boFactory::get)
                        .collect(Collectors.toList());
    }

    @Override
    public Optional<CashGameBO> findCashGame(CashGameRef cashGameRef)
    {
        return cashgameDao.findByOwnerRefAndLocalRef(cashGameRef.getUserRef().getGlobalRef(), cashGameRef.getLocalRef()).map(boFactory::get);
    }
}
