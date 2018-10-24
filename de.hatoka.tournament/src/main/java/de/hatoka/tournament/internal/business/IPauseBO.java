package de.hatoka.tournament.internal.business;

import java.util.Date;

public interface IPauseBO
{
    Integer getPosition();

    void setStartDate(Date date);
}
