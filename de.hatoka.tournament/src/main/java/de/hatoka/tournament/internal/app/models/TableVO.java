package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import de.hatoka.tournament.capi.business.TableBO;

public class TableVO
{
    @XmlAttribute
    private int number;
    private List<CompetitorVO> competitors = new ArrayList<>();

    public TableVO()
    {
    }

    public TableVO(TableBO table)
    {
        setNumber(table.getTableNo());
        competitors = table.getCompetitors().stream().map(competitor -> new CompetitorVO(competitor)).collect(Collectors.toList());
    }

    @XmlTransient
    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public List<CompetitorVO> getCompetitors()
    {
        return competitors;
    }

    public void setCompetitors(List<CompetitorVO> competitors)
    {
        this.competitors = competitors;
    }
}
