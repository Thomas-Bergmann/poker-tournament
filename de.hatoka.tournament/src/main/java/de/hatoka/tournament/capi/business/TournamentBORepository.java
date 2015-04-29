package de.hatoka.tournament.capi.business;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface TournamentBORepository
{
    TournamentBO createTournament(String name, Date date);
    TournamentBO getTournamentByID(String id);
    List<TournamentBO> getTournamenBOs();

    CashGameBO createCashGame(Date date);
    CashGameBO getCashGameByID(String id);
    List<CashGameBO> getCashGameBOs();

    File exportFile() throws IOException;
}
