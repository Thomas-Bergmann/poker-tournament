package de.hatoka.tournament.internal.remote.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.hatoka.tournament.capi.business.TableBO;
import de.hatoka.tournament.internal.remote.model.TableRO;

@Component
public class TableBO2RO
{
    private final CompetitorBO2RO bo2ro = new CompetitorBO2RO();

    public TableRO apply(TableBO table)
    {
        TableRO result = new TableRO();
        result.setNumber(table.getTableNo());
        result.setCompetitors(table.getCompetitors().stream().map(bo2ro).collect(Collectors.toList()));
        return result;
    }
}
