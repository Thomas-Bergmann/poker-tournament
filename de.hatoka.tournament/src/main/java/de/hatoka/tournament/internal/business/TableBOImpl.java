package de.hatoka.tournament.internal.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.CompetitorBO;
import de.hatoka.tournament.capi.business.TableBO;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TableBOImpl implements TableBO, ITableBO
{
    private final int number;
    private List<CompetitorBO> competitors = new ArrayList<>();

    public TableBOImpl(int number)
    {
        this.number = number;
    }

    @Override
    public void add(CompetitorBO competitor)
    {
        competitors.add(competitor);
        competitors.sort(new Comparator<CompetitorBO>()
        {
            @Override
            public int compare(CompetitorBO o1, CompetitorBO o2)
            {
                return o1.getSeatNo().compareTo(o2.getSeatNo());
            }
        });
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
