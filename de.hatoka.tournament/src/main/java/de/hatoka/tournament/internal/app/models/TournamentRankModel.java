package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentRankModel
{
    private TournamentVO tournament;
    private List<RankVO> ranks = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public List<String> getErrors()
    {
        return errors;
    }

    public void setErrors(List<String> errors)
    {
        this.errors = errors;
    }

    public TournamentVO getTournament()
    {
        return tournament;
    }

    public void setTournament(TournamentVO tournament)
    {
        this.tournament = tournament;
    }

    public List<RankVO> getRanks()
    {
        return ranks;
    }

    public void setRanks(List<RankVO> ranks)
    {
        this.ranks = ranks;
    }

    /**
     * Set calculated flags <li>ranks are added and sorted
     */
    public void fill()
    {
        fillLastPosition();
    }

    private void fillLastPosition()
    {
        RankVO lastRank = null;
        for (RankVO rank : ranks)
        {
            if (lastRank != null)
            {
                lastRank.setLastPosition(rank.getFirstPosition() - 1);
                lastRank.setLastPositionCalculated(true);
            }
            lastRank = rank;
            lastRank.setLastPositionCalculated(false);
        }
    }
}
