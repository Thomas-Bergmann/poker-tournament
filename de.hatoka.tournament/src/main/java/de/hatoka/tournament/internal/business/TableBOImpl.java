package de.hatoka.tournament.internal.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.TableBO;

public class TableBOImpl implements TableBO
{
    private final int number;
    private List<CompetitorBO> competitors = new ArrayList<>();

    public TableBOImpl(int number)
    {
        this.number = number;
    }

    public void addCompetitor(CompetitorBO competitor)
    {
        competitors.add(competitor);
    }

    @Override
    public int getTableNo()
    {
        return number;
    }

    @Override
    public List<CompetitorBO> getCompetitors()
    {
        return Collections.unmodifiableList(competitors);
    }
}
