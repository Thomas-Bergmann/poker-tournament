package de.hatoka.tournament.capi.business;

import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

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
}
