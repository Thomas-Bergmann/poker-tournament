package de.hatoka.tournament.capi.business;

import java.util.Collection;
import java.util.Date;

import de.hatoka.common.capi.business.BusinessObject;
import de.hatoka.common.capi.business.Money;

public interface TournamentBO extends BusinessObject
{
    /**
     * A player likes to attend to the tournament, the buy-in is not paid.
     * @param playerBO
     * @return
     */
    CompetitorBO assign(PlayerBO playerBO);

    Collection<CompetitorBO> getActiveCompetitors();
    /**
     * @return amount of money for all active players.
     */
    Money getAverageInplay();

    Money getBuyIn();

    Collection<CompetitorBO> getCompetitors();

    Date getDate();

    String getName();

    /**
     * @return amount of money for all active players.
     */
    Money getSumInplay();

    boolean isCompetitor(PlayerBO player);

    void seatOpen(CompetitorBO competitorBO);

    void setBuyIn(Money instance);

    void unassign(CompetitorBO competitorBO);
}
