package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentTableModel
{
    private TournamentVO tournament;
    private List<TableVO> tables = new ArrayList<>();
    private List<CompetitorVO> placedCompetitors = new ArrayList<>();

    public TournamentVO getTournament()
    {
        return tournament;
    }

    public void setTournament(TournamentVO tournament)
    {
        this.tournament = tournament;
    }

    public List<TableVO> getTables()
    {
        return tables;
    }

    public void setTables(List<TableVO> tables)
    {
        this.tables = tables;
    }

    public List<CompetitorVO> getPlacedCompetitors()
    {
        return placedCompetitors;
    }

    public void setPlacedCompetitors(List<CompetitorVO> placedCompetitors)
    {
        this.placedCompetitors = placedCompetitors;
    }
}
