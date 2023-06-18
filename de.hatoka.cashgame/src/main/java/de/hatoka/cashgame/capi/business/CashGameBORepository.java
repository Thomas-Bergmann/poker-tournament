package de.hatoka.cashgame.capi.business;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import de.hatoka.user.capi.business.UserRef;

/**
 * Access to tournaments of an user
 */
public interface CashGameBORepository
{
    CashGameBO createCashGame(UserRef userRef, String localRef, Date date);
    Optional<CashGameBO> findCashGame(CashGameRef cashGameRef);
    List<CashGameBO> getCashGames(UserRef userRef);
}
