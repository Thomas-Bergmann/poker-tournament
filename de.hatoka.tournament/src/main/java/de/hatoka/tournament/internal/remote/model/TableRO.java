package de.hatoka.tournament.internal.remote.model;

import java.util.ArrayList;
import java.util.List;

public class TableRO
{
    private int number;
    private List<CompetitorRO> competitors = new ArrayList<>();

    public TableRO()
    {
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public List<CompetitorRO> getCompetitors()
    {
        return competitors;
    }

    public void setCompetitors(List<CompetitorRO> competitors)
    {
        this.competitors = competitors;
    }
}
