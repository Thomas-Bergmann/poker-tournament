package de.hatoka.tournament.internal.app.models;

import java.net.URI;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.common.capi.app.model.SelectVO;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.GameBO;
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
    private Integer initialStack;
    private Integer smallestTable;
    private Integer largestTable;
    private SelectVO reBuyOption = new SelectVO();

    public TournamentVO()
    {
    }

    public TournamentVO(TournamentBO tournamentBO, URI uri)
    {
        this((GameBO) tournamentBO, uri);
        name = tournamentBO.getName();
        date = tournamentBO.getStartTime();
        initialStack = tournamentBO.getInitialStacksize();
        smallestTable = tournamentBO.getMininumNumberOfPlayersPerTable();
        largestTable = tournamentBO.getMaximumNumberOfPlayersPerTable();
    }

    public TournamentVO(CashGameBO cashGameBO, URI uri)
    {
        this((GameBO) cashGameBO, uri);
        name = cashGameBO.getName();
        date = cashGameBO.getDate();
        average = cashGameBO.getAverageInplay() == null ? null : new MoneyVO(cashGameBO.getAverageInplay());
    }

    public TournamentVO(GameBO gameBO, URI uri)
    {
        id = gameBO.getID();
        buyIn = gameBO.getBuyIn() == null ? null : new MoneyVO(gameBO.getBuyIn());
        sumInPlay = gameBO.getSumInplay() == null ? null : new MoneyVO(gameBO.getSumInplay());
        competitorsSize = gameBO.getCompetitors().size();
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

    public Integer getInitialStack()
    {
        return initialStack;
    }

    public void setInitialStack(Integer initialStack)
    {
        this.initialStack = initialStack;
    }

    public Integer getSmallestTable()
    {
        return smallestTable;
    }

    public void setSmallestTable(Integer smallestTable)
    {
        this.smallestTable = smallestTable;
    }

    public Integer getLargestTable()
    {
        return largestTable;
    }

    public void setLargestTable(Integer largestTable)
    {
        this.largestTable = largestTable;
    }

    public SelectVO getReBuyOption()
    {
        return reBuyOption;
    }

    public void setReBuyOption(SelectVO reBuyOption)
    {
        this.reBuyOption = reBuyOption;
    }
}
