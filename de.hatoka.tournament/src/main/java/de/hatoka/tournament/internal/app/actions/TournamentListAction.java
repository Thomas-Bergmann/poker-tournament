package de.hatoka.tournament.internal.app.actions;

import java.net.URI;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.business.Money;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.models.TournamentListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;
import de.hatoka.tournament.internal.app.servlets.CashGameListService;
import de.hatoka.tournament.internal.app.servlets.TournamentListService;

public class TournamentListAction
{
    @Inject
    private TournamentBusinessFactory factory;

    private final String accountRef;

    public TournamentListAction(String accountRef)
    {
        this.accountRef = accountRef;
    }

    public TournamentBO createCashGame(String name, Date date, String buyIn)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        TournamentBO result = tournamentBORepository.createCashGame(date);
        result.setBuyIn(Money.getInstance(buyIn));
        return result;
    }

    public TournamentBO createTournament(String name, Date date, String buyIn)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        TournamentBO result = tournamentBORepository.createTournament(name, date);
        result.setBuyIn(Money.getInstance(buyIn));
        return result;
    }

    public void deleteTournaments(List<String> identifiers)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        for (String id : identifiers)
        {
            tournamentBORepository.getByID(id).remove();
        }
    }

    public TournamentListModel getTournamentListModel(UriBuilder uriBuilder)
    {
        return getListModel(uriBuilder, false);
    }
    public TournamentListModel getCashGameListModel(UriBuilder uriBuilder)
    {
        return getListModel(uriBuilder, false);
    }

    private TournamentListModel getListModel(UriBuilder uriBuilder, boolean isCashGame)
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        final TournamentListModel model = new TournamentListModel();
        List<TournamentBO> tournamentBOs = isCashGame ? tournamentBORepository.getCashGameBOs() : tournamentBORepository.getTournamenBOs();
        Consumer<TournamentBO> mapper = new Consumer<TournamentBO>()
        {
            @Override
            public void accept(TournamentBO bo)
            {
                model.getTournaments().add(new TournamentVO(bo, uriBuilder.build(bo.getID())));
            }
        };
        Comparator<TournamentBO> sorter = new Comparator<TournamentBO>()
        {
            @Override
            public int compare(TournamentBO a, TournamentBO b)
            {
                return a.getDate().compareTo(b.getDate());
            }
        };
        tournamentBOs.sort(sorter);
        tournamentBOs.forEach(mapper);
        return model;
    }

    public FrameModel getMainFrameModel(String content, String titleKey, UriInfo info, boolean isCashGame)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, CashGameListService.class, "list"));
        model.addMainMenu("menu.list.tournaments", getUri(info, TournamentListService.class, "list"), !isCashGame);
        model.addMainMenu("menu.list.cashgames", getUri(info, CashGameListService.class, "list"), isCashGame);

        model.addSiteMenu("menu.list.tournaments", getUri(info, TournamentListService.class, "list"), getTournamentsSize(),
                        getUri(info, TournamentListService.class, "add"), !isCashGame);
        model.addSiteMenu("menu.list.cashgames", getUri(info, CashGameListService.class, "list"), getCashGamesSize(),
                        getUri(info, CashGameListService.class, "add"), isCashGame);
        return model;
    }

    private Integer getTournamentsSize()
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        return tournamentBORepository.getTournamenBOs().size();
    }

    private Integer getCashGamesSize()
    {
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        return tournamentBORepository.getCashGameBOs().size();
    }

    public URI getUri(UriInfo info, Class<?> resource, String methodName)
    {
        return info.getBaseUriBuilder().path(resource).path(resource, methodName).build();
    }

}
