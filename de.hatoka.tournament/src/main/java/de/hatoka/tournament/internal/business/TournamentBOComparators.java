package de.hatoka.tournament.internal.business;

import java.util.Comparator;

import de.hatoka.tournament.capi.business.TournamentBO;

public final class TournamentBOComparators
{
    private TournamentBOComparators()
    {
    }

    public static final Comparator<TournamentBO> BY_STARTTIME = new Comparator<TournamentBO>()
    {
        @Override
        public int compare(TournamentBO a, TournamentBO b)
        {
            return a.getStartTime().compareTo(b.getStartTime());
        }
    };
}
