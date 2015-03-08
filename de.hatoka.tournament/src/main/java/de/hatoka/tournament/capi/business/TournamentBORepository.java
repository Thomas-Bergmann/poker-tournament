package de.hatoka.tournament.capi.business;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface TournamentBORepository
{
    TournamentBO createTournament(String name, Date date);

    TournamentBO createCashGame(Date date);

    TournamentBO getByID(String id);

    List<TournamentBO> getTournamenBOs();

    File exportFile() throws IOException;

    List<TournamentBO> getCashGameBOs();
}
