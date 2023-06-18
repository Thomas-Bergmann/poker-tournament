package de.hatoka.tournament.internal.business;

import java.util.Date;

import de.hatoka.tournament.capi.business.BlindLevelBO;

public interface IBlindLevelBO extends BlindLevelBO
{

    void setStartDate(Date date);

}
