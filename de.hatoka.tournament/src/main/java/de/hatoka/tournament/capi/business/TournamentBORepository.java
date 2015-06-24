package de.hatoka.tournament.capi.business;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import de.hatoka.common.capi.business.Warning;
import de.hatoka.tournament.capi.entities.TournamentModel;

public interface TournamentBORepository
{
    TournamentBO createTournament(String name, Date date);

    TournamentBO createTournament(String externalRef, String name, Date date);

    TournamentBO getTournamentByID(String id);

    List<TournamentBO> getTournaments();

    CashGameBO createCashGame(Date date);

    CashGameBO createCashGame(String externalRef, Date date);

    CashGameBO getCashGameByID(String id);

    List<CashGameBO> getCashGames();

    void exportXML(Writer writer) throws JAXBException;

    /**
     * Imports the XML stream of tournaments, see structure {@link TournamentModel}.
     * The transactions are handled, and encapsulate each tournament.
     *
     * @param resourceStream
     * @return list of warnings
     */
    List<Warning> importXML(InputStream resourceStream) throws IOException;
}
