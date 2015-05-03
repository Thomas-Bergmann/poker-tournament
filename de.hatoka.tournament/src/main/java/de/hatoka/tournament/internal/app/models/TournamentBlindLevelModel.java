package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.tournament.internal.business.TournamentTools;

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

    /**
     * call {@link #setTournament(TournamentVO)} with a valid start date before
     */
    public void fillTime()
    {
        if (tournament == null || tournament.getDate() == null)
        {
            throw new IllegalStateException();
        }
        Date nextStart = tournament.getDate();
        int maxSmallBlind = 0;
        int maxAnte = 0;
        int maxDurationLevel = 15;
        for(BlindLevelVO blindLevel : blindLevels)
        {
            blindLevel.setEstStartDateTime(nextStart);
            nextStart = new Date(nextStart.getTime() + (blindLevel.getDuration() * 60_000));
            blindLevel.setEstEndDateTime(nextStart);
            maxSmallBlind = Math.max(blindLevel.getSmallBlind(), maxSmallBlind);
            maxAnte = Math.max(blindLevel.getAnte(), maxAnte);
            if (!blindLevel.isPause())
            {
                maxDurationLevel = Math.max(blindLevel.getDuration(), maxDurationLevel);
            }
        }
        for(BlindLevelVO blindLevel : prefilled)
        {
            if (blindLevel.getSmallBlind() == 0)
            {
                maxSmallBlind = TournamentTools.getNextSmallBlind(maxSmallBlind);
                blindLevel.setSmallBlind(maxSmallBlind);
                blindLevel.setBigBlind(TournamentTools.getBigBlind(maxSmallBlind));
                blindLevel.setAnte(maxAnte);
            }
            if (blindLevel.getDuration() == 0)
            {
                blindLevel.setDuration(maxDurationLevel);
            }
            blindLevel.setEstStartDateTime(nextStart);
            nextStart = new Date(nextStart.getTime() + (blindLevel.getDuration() * 60_000));
            blindLevel.setEstEndDateTime(nextStart);
        }
    }

}
