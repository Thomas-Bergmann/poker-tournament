package de.hatoka.tournament.internal.app.models;

import java.net.URI;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.tournament.capi.business.TournamentBO;

@XmlRootElement
public class TournamentVO
{
    private String id;
    private String name;
    private Date date;
    private MoneyVO buyIn;
    private URI uri;
    private MoneyVO average;
    private MoneyVO sumInPlay;
    private Integer competitorsSize;

    public TournamentVO()
    {
    }

    public TournamentVO(TournamentBO tournamentBO, URI uri)
    {
        id = tournamentBO.getID();
        name = tournamentBO.getName();
        date = tournamentBO.getDate();
        buyIn = tournamentBO.getBuyIn() == null ? null : new MoneyVO(tournamentBO.getBuyIn());
        average = tournamentBO.getAverageInplay() == null ? null : new MoneyVO(tournamentBO.getAverageInplay());
        sumInPlay = tournamentBO.getSumInplay() == null ? null : new MoneyVO(tournamentBO.getSumInplay());
        competitorsSize = tournamentBO.getCompetitors().size();
        this.uri = uri;
    }

    public MoneyVO getAverage()
    {
        return average;
    }

    public MoneyVO getBuyIn()
    {
        return buyIn;
    }

    public Date getDate()
    {
        return date;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public MoneyVO getSumInPlay()
    {
        return sumInPlay;
    }

    public URI getUri()
    {
        return uri;
    }

    public void setAverage(MoneyVO average)
    {
        this.average = average;
    }

    public void setBuyIn(MoneyVO buyIn)
    {
        this.buyIn = buyIn;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSumInPlay(MoneyVO sumInPlay)
    {
        this.sumInPlay = sumInPlay;
    }

    public void setUri(URI uri)
    {
        this.uri = uri;
    }

    public Integer getCompetitorsSize()
    {
        return competitorsSize;
    }

    public void setCompetitorsSize(Integer competitorsSize)
    {
        this.competitorsSize = competitorsSize;
    }
}
