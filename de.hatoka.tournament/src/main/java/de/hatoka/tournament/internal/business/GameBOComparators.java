package de.hatoka.tournament.internal.business;

import java.util.Comparator;

import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.TournamentBO;

public final class GameBOComparators
{
    private GameBOComparators()
    {
    }

    public static final Comparator<CashGameBO> DEFAULT_CASHGAME = new Comparator<CashGameBO>()
    {
        @Override
        public int compare(CashGameBO a, CashGameBO b)
        {
            return a.getDate().compareTo(b.getDate());
        }
    };

    public static final Comparator<TournamentBO> DEFAULT_TOURNAMENT = new Comparator<TournamentBO>()
    {
        @Override
        public int compare(TournamentBO a, TournamentBO b)
        {
            return a.getStartTime().compareTo(b.getStartTime());
        }
    };
}
