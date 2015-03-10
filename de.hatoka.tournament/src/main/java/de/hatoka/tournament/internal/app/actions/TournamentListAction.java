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
import de.hatoka.tournament.internal.app.models.TournamentListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TournamentListAction
{
    @Inject
    private TournamentBusinessFactory factory;

    private final String accountRef;

    public TournamentListAction(String accountRef)
    {
        this.accountRef = accountRef;
    }

    public TournamentBO createCashGame(Date date, String buyIn)
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
        return getListModel(uriBuilder, true);
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

    public URI getUri(UriInfo info, Class<?> resource, String methodName)
    {
        return info.getBaseUriBuilder().path(resource).path(resource, methodName).build();
    }

}
