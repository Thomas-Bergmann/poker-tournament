package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentTableModel
{
    private TournamentVO tournament;
    private List<TableVO> tables = new ArrayList<>();

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

}
