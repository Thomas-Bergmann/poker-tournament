package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentPlayerListModel
{
    private TournamentVO tournament;
    private List<CompetitorVO> competitors = new ArrayList<>();
    private List<PlayerVO> unassignedPlayers = new ArrayList<>();

    public TournamentPlayerListModel()
    {

    }

    public List<CompetitorVO> getCompetitors()
    {
        return competitors;
    }

    public TournamentVO getTournament()
    {
        return tournament;
    }

    public List<PlayerVO> getUnassignedPlayers()
    {
        return unassignedPlayers;
    }

    public void setCompetitors(List<CompetitorVO> competitors)
    {
        this.competitors = competitors;
    }

    public void setTournament(TournamentVO tournament)
    {
        this.tournament = tournament;
    }

    public void setUnassignedPlayers(List<PlayerVO> unassignedPlayers)
    {
        this.unassignedPlayers = unassignedPlayers;
    }

}
