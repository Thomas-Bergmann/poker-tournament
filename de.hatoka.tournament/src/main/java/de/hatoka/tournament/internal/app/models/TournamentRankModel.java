package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentRankModel
{
    private TournamentVO tournament;
    private List<RankVO> ranks = new ArrayList<>();
    private List<RankVO> prefilled = new ArrayList<>();
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

    public List<RankVO> getPrefilled()
    {
        return prefilled;
    }

    public void setPrefilled(List<RankVO> prefilled)
    {
        this.prefilled = prefilled;
    }
}
