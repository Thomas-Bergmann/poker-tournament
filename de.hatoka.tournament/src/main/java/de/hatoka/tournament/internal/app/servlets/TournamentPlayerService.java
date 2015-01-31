package de.hatoka.tournament.internal.app.servlets;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.internal.app.actions.TournamentAction;
import de.hatoka.tournament.internal.app.models.TournamentPlayerListModel;

@Path("/tournament/{id}")
public class TournamentPlayerService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

    @PathParam("id")
    private String tournamentID;
    @Context
    private UriInfo info;

    private final AccountService accountService;

    public TournamentPlayerService()
    {
        super(RESOURCE_PREFIX);
        accountService = new AccountService(this);
    }

    @POST
    @Path("/actionPlayerList")
    public Response actionPlayerList(@FormParam("competitorID") List<String> identifiers,
                    @FormParam("delete") String deleteButton, @FormParam("seatopen") String seatOpenButton, @FormParam("rebuy") String rebuyButton, @FormParam("amount") String amount)
    {
        if (deleteButton != null && !deleteButton.isEmpty())
        {
            return unassignPlayers(identifiers);
        }
        if (rebuyButton != null && !rebuyButton.isEmpty())
        {
            return rebuyPlayers(identifiers, amount);
        }
        if (seatOpenButton != null && !seatOpenButton.isEmpty())
        {
            return seatOpenPlayers(identifiers, amount);
        }
        return redirectPlayers();
    }

    @POST
    @Path("/assignPlayer")
    public Response assignPlayer(@FormParam("playerID") String playerID)
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        TournamentAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.assignPlayer(tournamentID, playerID);
            }
        });
        return redirectPlayers();
    }

    @POST
    @Path("/createPlayer")
    public Response createPlayer(@FormParam("name") String name)
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        TournamentAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createPlayer(tournamentID, name);
            }
        });
        return redirectPlayers();
    }

    private TournamentAction getAction(String accountRef)
    {
        TournamentAction tournamentAction = new TournamentAction(accountRef);
        getInjector().injectMembers(tournamentAction);
        return tournamentAction;
    }

    @GET
    @Path("/players.html")
    public Response players()
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        final TournamentPlayerListModel model = getAction(accountRef).getPlayerListModel(tournamentID,
                        getUriBuilder(TournamentListService.class, "list").build(),
                        getUriBuilder(TournamentPlayerService.class, "players"));
        return renderStyleSheet(model, "tournament_players.xslt", "tournament");
    }

    @POST
    @Path("/rebuyPlayers")
    public Response rebuyPlayers(@FormParam("competitorID") List<String> identifiers, @FormParam("rebuy") String rebuy)
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        TournamentAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.rebuyPlayers(tournamentID, identifiers, rebuy);
            }
        });
        return redirectPlayers();
    }

    private Response redirectPlayers()
    {
        return Response.seeOther(getUriBuilder(TournamentPlayerService.class, "players").build(tournamentID)).build();
    }

    @POST
    @Path("/seatOpenPlayers")
    public Response seatOpenPlayers(@FormParam("competitorID") List<String> identifiers, @FormParam("amount") String amount)
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        TournamentAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.seatOpenPlayers(tournamentID, identifiers, amount);
            }
        });
        return redirectPlayers();
    }


    @POST
    @Path("/unassignPlayers")
    public Response unassignPlayers(@FormParam("competitorID") List<String> identifiers)
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            return accountService.redirectLogin();
        }
        TournamentAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.unassignPlayers(tournamentID, identifiers);
            }
        });
        return redirectPlayers();
    }
}