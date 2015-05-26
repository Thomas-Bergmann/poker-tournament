package de.hatoka.tournament.internal.app.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.CashGameCompetitorBO;
import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;

public class CashGameAction extends GameAction<CashGameBO>
{
    public CashGameAction(String accountRef, CashGameBO cashGameBO, TournamentBusinessFactory factory)
    {
        super(accountRef, cashGameBO, factory);
    }

    /**
     *
     * @param competitorIDs
     * @param rebuyString
     * @return a list of error codes
     */
    public List<String> rebuyPlayers(Collection<String> competitorIDs, String rebuyString)
    {
        Money rebuy = null;
        CashGameBO cashGameBO = getGame();
        try
        {
            rebuy = Money.valueOf(rebuyString, cashGameBO.getSumInplay().getCurrency());
        }
        catch(NumberFormatException e)
        {
            return new ArrayList<String>(Arrays.asList("error.number.format"));
        }

        for (CashGameCompetitorBO competitorBO : cashGameBO.getCashGameCompetitors())
        {
            if (competitorIDs.contains(competitorBO.getID()))
            {
                if (competitorBO.isActive())
                {
                    competitorBO.rebuy(rebuy);
                }
                else
                {
                    competitorBO.buyin(rebuy);
                }
            }
        }
        return java.util.Collections.emptyList();
    }

    /**
     * @param playerBO
     * @param buyInString (optional)
     * @return list of error codes
     */
    public List<String> sitDown(PlayerBO playerBO, String buyInString)
    {
        CashGameBO cashGameBO = getGame();
        Money buyIn = null;
        if (buyInString == null || buyInString.isEmpty())
        {
            buyIn = cashGameBO.getBuyIn();
        }
        else
        {
            try
            {
                buyIn = Money.valueOf(buyInString, cashGameBO.getSumInplay().getCurrency());
            }
            catch(NumberFormatException e)
            {
                return new ArrayList<String>(Arrays.asList("error.number.format"));
            }
        }
        cashGameBO.sitDown(playerBO, buyIn);
        return java.util.Collections.emptyList();
    }

    public void seatOpenPlayers(List<String> identifiers, String restAmountString)
    {
        CashGameBO cashGameBO = getGame();
        Collection<CompetitorBO> activeCompetitors = cashGameBO.getActiveCompetitors();
        Money restAmount = activeCompetitors.size() == identifiers.size() ? cashGameBO.getSumInplay().divide(
                        identifiers.size()) : Money.valueOf(restAmountString, cashGameBO.getSumInplay()
                        .getCurrency());
        for (CompetitorBO competitorBO : activeCompetitors)
        {
            if (identifiers.contains(competitorBO.getID()))
            {
                cashGameBO.seatOpen(competitorBO, restAmount);
            }
        }
    }
}
