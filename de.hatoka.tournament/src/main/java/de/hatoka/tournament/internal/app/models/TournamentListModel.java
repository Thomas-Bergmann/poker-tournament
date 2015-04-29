package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentListModel
{
    private List<TournamentVO> tournaments = new ArrayList<>();

    public List<TournamentVO> getTournaments()
    {
        tournaments.sort(new Comparator<TournamentVO>()
                        {
            @Override
            public int compare(TournamentVO o1, TournamentVO o2)
            {
                return o2.getDate().compareTo(o1.getDate());
            }
                        });
        return tournaments;
    }

    public void setTournaments(List<TournamentVO> tournaments)
    {
        this.tournaments = tournaments;
    }
}
