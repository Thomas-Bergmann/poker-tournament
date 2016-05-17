package de.hatoka.tournament.internal.app.actions;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.TournamentListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;
import de.hatoka.tournament.internal.business.GameBOComparators;

public class TournamentListAction
{
    @Inject
    private TournamentBusinessFactory factory;

    @Inject
    private RequestUserRefProvider userRefProvider;

    public CashGameBO createCashGame(Date date, String buyIn)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(userRefProvider.getUserRef());
        CashGameBO result = tournamentBORepository.createCashGame(date);
        result.setBuyIn(Money.valueOf(buyIn));
        return result;
    }

    public TournamentBO createTournament(String name, Date date, String buyIn)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(userRefProvider.getUserRef());
        TournamentBO result = tournamentBORepository.createTournament(name, date);
        result.setBuyIn(Money.valueOf(buyIn));

        result.createRank(1, 1, new BigDecimal("0.5"));
        result.createRank(2, 2, new BigDecimal("0.3"));
        result.createRank(3, 3, new BigDecimal("0.2"));
        return result;
    }

    public TournamentListModel getTournamentListModel(UriBuilder uriBuilder)
    {
        return getListModel(uriBuilder, false);
    }

    public TournamentListModel getCashGameListModel(UriBuilder uriBuilder)
    {
        return getListModel(uriBuilder, true);
    }

    private TournamentListModel getListModel(UriBuilder uriBuilder, boolean filterGashGames)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(userRefProvider.getUserRef());
        final TournamentListModel model = new TournamentListModel();
        if(filterGashGames)
        {
            addCashGames(uriBuilder, tournamentBORepository, model);
        }
        else
        {
            addTournaments(uriBuilder, tournamentBORepository, model);
        }
        return model;
    }

    private static void addCashGames(UriBuilder uriBuilder, TournamentBORepository tournamentBORepository,
                    final TournamentListModel model)
    {
        List<CashGameBO> cashGameBOs = tournamentBORepository.getCashGames();
        Consumer<CashGameBO> mapper = bo -> model.getTournaments().add(new TournamentVO(bo, uriBuilder.build(bo.getID())));
        cashGameBOs.sort(GameBOComparators.DEFAULT_CASHGAME);
        cashGameBOs.forEach(mapper);
    }

    private static void addTournaments(UriBuilder uriBuilder, TournamentBORepository tournamentBORepository,
                    final TournamentListModel model)
    {
        List<TournamentBO> cashGameBOs = tournamentBORepository.getTournaments();
        Consumer<TournamentBO> mapper = bo -> model.getTournaments().add(new TournamentVO(bo, uriBuilder.build(bo.getID())));
        cashGameBOs.sort(GameBOComparators.DEFAULT_TOURNAMENT);
        cashGameBOs.forEach(mapper);
    }

    public URI getUri(UriInfo info, Class<?> resource, String methodName)
    {
        return info.getBaseUriBuilder().path(resource).path(resource, methodName).build();
    }

    public void deleteTournaments(List<String> identifiers)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(userRefProvider.getUserRef());
        for (String id : identifiers)
        {
            tournamentBORepository.getTournamentByID(id).remove();
        }
    }

    public void deleteCashGames(List<String> identifiers)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(userRefProvider.getUserRef());
        for (String id : identifiers)
        {
            tournamentBORepository.getCashGameByID(id).remove();
        }
    }

}
