package de.hatoka.tournament.internal.app.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.CompetitorBO;

public class CashGameAction
{
    private final CashGameBO cashGameBO;

    public CashGameAction(CashGameBO cashGameBO)
    {
        this.cashGameBO = cashGameBO;
    }

    public List<String> rebuyPlayers(List<String> identifiers, String rebuyString)
    {
        Money rebuy = null;
        try
        {
            rebuy = Money.getInstance(rebuyString, cashGameBO.getSumInplay().getCurrency());
        }
        catch(NumberFormatException e)
        {
            return new ArrayList<String>(Arrays.asList("error.number.format"));
        }

        for (CompetitorBO competitorBO : cashGameBO.getCompetitors())
        {
            if (identifiers.contains(competitorBO.getID()))
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

    public void seatOpenPlayers(List<String> identifiers, String restAmountString)
    {
        Collection<CompetitorBO> activeCompetitors = cashGameBO.getActiveCompetitors();
        Money restAmount = activeCompetitors.size() == identifiers.size() ? cashGameBO.getSumInplay().divide(
                        identifiers.size()) : Money.getInstance(restAmountString, cashGameBO.getSumInplay()
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
