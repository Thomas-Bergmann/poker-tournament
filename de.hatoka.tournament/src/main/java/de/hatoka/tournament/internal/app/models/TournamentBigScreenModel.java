package de.hatoka.tournament.internal.app.models;

import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.tournament.capi.business.BlindLevelBO;
import de.hatoka.tournament.capi.business.TournamentBO;

@XmlRootElement
public class TournamentBigScreenModel
{
    private BlindLevelVO currentBlindLevel;
    private BlindLevelVO nextBlindLevel;
    private int averageStackSize = 0;
    private int currentAmountPlayer = 0;
    private int maxAmountPlayers = 0;

    public TournamentBigScreenModel()
    {
    }

    public TournamentBigScreenModel(TournamentBO tournamentBO)
    {
        this.currentAmountPlayer = tournamentBO.getActiveCompetitors().size();
        this.maxAmountPlayers = tournamentBO.getCompetitors().size();
        BlindLevelBO currentBlindLevelBO = tournamentBO.getCurrentBlindLevel();
        this.currentBlindLevel = currentBlindLevelBO == null ? null : new BlindLevelVO(currentBlindLevelBO);
        BlindLevelBO nextBlindLevelBO = tournamentBO.getNextBlindLevel();
        this.nextBlindLevel = nextBlindLevelBO == null ? null : new BlindLevelVO(nextBlindLevelBO);
    }

    public BlindLevelVO getCurrentBlindLevel()
    {
        return currentBlindLevel;
    }

    public void setCurrentBlindLevel(BlindLevelVO currentBlindLevel)
    {
        this.currentBlindLevel = currentBlindLevel;
    }

    public BlindLevelVO getNextBlindLevel()
    {
        return nextBlindLevel;
    }

    public void setNextBlindLevel(BlindLevelVO nextBlindLevel)
    {
        this.nextBlindLevel = nextBlindLevel;
    }

    public int getAverageStackSize()
    {
        return averageStackSize;
    }

    public void setAverageStackSize(int averageStackSize)
    {
        this.averageStackSize = averageStackSize;
    }

    public int getCurrentAmountPlayer()
    {
        return currentAmountPlayer;
    }

    public void setCurrentAmountPlayer(int currentAmountPlayer)
    {
        this.currentAmountPlayer = currentAmountPlayer;
    }

    public int getMaxAmountPlayers()
    {
        return maxAmountPlayers;
    }

    public void setMaxAmountPlayers(int maxAmountPlayers)
    {
        this.maxAmountPlayers = maxAmountPlayers;
    }
}
