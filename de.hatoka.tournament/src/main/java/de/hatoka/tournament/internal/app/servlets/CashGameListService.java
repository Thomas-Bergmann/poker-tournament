package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.config.TournamentConfiguration;
import de.hatoka.tournament.internal.app.actions.TournamentListAction;
import de.hatoka.tournament.internal.app.models.TournamentListModel;

@Path("/cashgames")
public class CashGameListService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

    @Context
    private UriInfo info;

    private final AccountService accountService;

    public CashGameListService()
    {
        super(RESOURCE_PREFIX);
        accountService = new AccountService(this);
    }

    @POST
    @Path("/action")
    public Response action(@FormParam("tournamentID") List<String> identifiers, @FormParam("delete") String deleteButton)
    {
        if (isButtonPressed(deleteButton))
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
        final TournamentListModel model = getAction(accountRef).getCashGameListModel(getUriBuilderPlayers());
        try
        {
            String content = renderStyleSheet(model, "tournament_list.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.list.cashgame")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    private UriBuilder getUriBuilderPlayers()
    {
        return getUriBuilder(CashGameCompetitorService.class, "players");
    }

    /**
     * Lists all tournaments
     *
     * @return
     */
    @GET
    @Path("/add.html")
    public Response add()
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        final TournamentListModel model = getAction(accountRef).getCashGameListModel(getUriBuilderPlayers());
        try
        {
            String content = renderStyleSheet(model, "tournament_add.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.create.cashgame")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    @GET
    @Path("/register.html")
    public Response register(@DefaultValue("") @QueryParam("accountID") final String accountID,
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
        return Response.seeOther(getUriBuilder(CashGameListService.class, "list").build()).cookie(cookies).build();
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentListAction action = getAction(accountService.getAccountRef());
        return renderStyleSheet(action.getMainFrameModel(content, titleKey, getInfo(), true), "tournament_frame.xslt", getXsltProcessorParameter("tournament"));
    }
}