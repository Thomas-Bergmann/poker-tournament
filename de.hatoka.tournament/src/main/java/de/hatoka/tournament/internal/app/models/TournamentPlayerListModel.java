package de.hatoka.tournament.internal.app.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentPlayerListModel
{
    private URI listUri;
    private TournamentVO tournament;
    private List<CompetitorVO> competitors = new ArrayList<>();
    private List<PlayerVO> unassignedPlayers = new ArrayList<>();

    public TournamentPlayerListModel()
    {

    }

    public TournamentPlayerListModel(URI listTournamentURI)
    {
        listUri = listTournamentURI;
    }

    public List<CompetitorVO> getCompetitors()
    {
        return competitors;
    }

    public URI getListUri()
    {
        return listUri;
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

    public void setListUri(URI listUri)
    {
        this.listUri = listUri;
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
