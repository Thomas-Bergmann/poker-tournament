package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TournamentBlindLevelModel
{
    private TournamentVO tournament;
    private List<BlindLevelVO> blindLevels = new ArrayList<>();
    private List<BlindLevelVO> prefilled = new ArrayList<>();
    private List<String> errors = new ArrayList<>();

    public List<BlindLevelVO> getBlindLevels()
    {
        return blindLevels;
    }

    public void setBlindLevels(List<BlindLevelVO> blindLevels)
    {
        this.blindLevels = blindLevels;
    }

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

    public List<BlindLevelVO> getPrefilled()
    {
        return prefilled;
    }

    public void setPrefilled(List<BlindLevelVO> prefilled)
    {
        this.prefilled = prefilled;
    }

    public void fillTime(Date start)
    {
        Date nextStart = start;
        for(BlindLevelVO blindLevel : blindLevels)
        {
            blindLevel.setEstStartDateTime(nextStart);
            nextStart = new Date(nextStart.getTime() + (blindLevel.getDuration() * 60_000));
            blindLevel.setEstEndDateTime(nextStart);
        }
        for(BlindLevelVO blindLevel : prefilled)
        {
            blindLevel.setEstStartDateTime(nextStart);
            nextStart = new Date(nextStart.getTime() + (blindLevel.getDuration() * 60_000));
            blindLevel.setEstEndDateTime(nextStart);
        }
    }
}
