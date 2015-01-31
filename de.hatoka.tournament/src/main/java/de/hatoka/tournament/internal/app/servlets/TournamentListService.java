package de.hatoka.tournament.internal.app.servlets;

import java.util.Date;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.config.TournamentConfiguration;
import de.hatoka.tournament.internal.app.actions.TournamentListAction;
import de.hatoka.tournament.internal.app.models.TournamentListModel;

@Path("/tournaments")
public class TournamentListService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

    @Context
    private UriInfo info;

    private final AccountService accountService;
    public TournamentListService()
    {
        super(RESOURCE_PREFIX);
        accountService = new AccountService(this);
    }

    @POST
    @Path("/action")
    public Response action(@FormParam("tournamentID") List<String> identifiers, @FormParam("delete") String deleteButton)
    {
        if (deleteButton != null && !deleteButton.isEmpty())
        {
            return delete(identifiers);
        }
        return redirectList();
    }

    @POST
    @Path("/create")
    public Response create(@FormParam("name") String name, @FormParam("buyIn") String buyIn)
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        TournamentListAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createTournament(name, new Date(), buyIn);
            }
        });
        return redirectList();
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam("tournamentID") final List<String> identifiers)
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        TournamentListAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.deleteTournaments(identifiers);
            }
        });
        return redirectList();
    }

    private TournamentListAction getAction(String accountRef)
    {
        TournamentListAction action = new TournamentListAction(accountRef);
        getInjector().injectMembers(action);
        return action;
    }

    /**
     * Lists all tournaments
     *
     * @return
     */
    @GET
    @Path("/list.html")
    public Response list()
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        final TournamentListModel model = getAction(accountRef).getListModel(
                        getUriBuilder(TournamentPlayerService.class, "players"));
        return renderStyleSheet(model, "tournament_list.xslt", "tournament");
    }

    @GET
    @Path("/register.html")
    public Response register( @DefaultValue("") @QueryParam("accountID")  final String accountID,
                              @DefaultValue("") @QueryParam("accountSign") final String accountSign)
    {
        if (accountID == null || accountID.isEmpty())
        {
            return Response.seeOther(
                            info.getBaseUriBuilder().uri(getInstance(TournamentConfiguration.class).getLoginURI())
                                            .queryParam("origin", info.getRequestUri()).build()).build();
        }
        if (accountService.getAccountRef(accountID, accountSign) == null)
        {
            return accountService.redirectLogin();
        }
        List<NewCookie> cookies = accountService.getCookies(accountID, accountSign);
        return redirectList(cookies.toArray(new NewCookie[cookies.size()]));
    }


    private Response redirectList(NewCookie... cookies)
    {
        return Response.seeOther(getUriBuilder(TournamentListService.class, "list").build()).cookie(cookies).build();
    }
}