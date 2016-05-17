package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.business.PlayerBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.CashGameAction;
import de.hatoka.tournament.internal.app.actions.PlayerAction;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.models.HistoryModel;
import de.hatoka.tournament.internal.app.models.TournamentPlayerListModel;

@Path("/cashgame/{id}/competitors")
public class CashGameCompetitorService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;

    private final MenuFactory menuFactory = new MenuFactory();

    public CashGameCompetitorService()
    {
        super(RESOURCE_PREFIX);
    }

    private PlayerAction getPlayerAction()
    {
        return new PlayerAction(getUserRef(), getInstance(TournamentBusinessFactory.class));
    }

    private CashGameAction getCashGameAction()
    {
        String accountRef = getUserRef();
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        return new CashGameAction(accountRef, tournamentBORepository.getCashGameByID(tournamentID), factory);
    }

    @POST
    @Path("/actionList")
    public Response actionPlayerList(@FormParam("competitorID") List<String> identifiers,
                    @FormParam("delete") String deleteButton, @FormParam("seatopen") String seatOpenButton,
                    @FormParam("sort") String sortButton, @FormParam("rebuy") String rebuyButton,
                    @FormParam("amount") String amount)
    {
        if (isButtonPressed(sortButton))
        {
            return sortPlayers();
        }
        if (isButtonPressed(deleteButton))
        {
            return unassignPlayers(identifiers);
        }
        if (isButtonPressed(rebuyButton))
        {
            return rebuyPlayers(identifiers, amount);
        }
        if (isButtonPressed(seatOpenButton))
        {
            return seatOpenPlayers(identifiers, amount);
        }
        return redirectPlayers();
    }

    @POST
    @Path("/sort")
    public Response sortPlayers()
    {
        CashGameAction action = getCashGameAction();
        runInTransaction(() -> action.sortPlayers());
        return redirectPlayers();
    }

    @POST
    @Path("/assign")
    public Response assignPlayer(@FormParam("playerID") String playerID, @FormParam("amount") String amount)
    {
        CashGameAction action = getCashGameAction();
        runInTransaction(() -> {
            PlayerBO playerBO = getPlayerAction().getPlayer(playerID);
            action.sitDown(playerBO, amount);
        });
        return redirectAddPlayer();
    }

    @POST
    @Path("/create")
    public Response createPlayer(@FormParam("name") String name, @FormParam("amount") String amount)
    {
        CashGameAction action = getCashGameAction();
        runInTransaction(() -> {
            PlayerBO playerBO = getPlayerAction().createPlayer(name);
            action.sitDown(playerBO, amount);
        });
        return redirectAddPlayer();
    }

    @GET
    @Path("/list.html")
    public Response list()
    {
        return players(Collections.emptyList());
    }

    @GET
    @Path("/players.html")
    public Response players(@QueryParam("errors") List<String> errors)
    {
        CashGameAction action = getCashGameAction();
        final TournamentPlayerListModel model = action.getPlayerListModel(
                        getUriBuilder(CashGameListService.class, METHOD_NAME_LIST).build(),
                        getUriBuilder(CashGameCompetitorService.class, METHOD_NAME_LIST));
        model.getErrors().addAll(errors);
        try
        {
            String content = renderStyleSheet(model, "cashgame_players.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "players")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    @GET
    @Path("/history.html")
    public Response history()
    {
        CashGameAction action = getCashGameAction();
        final HistoryModel model = action.getHistoryModel();
        try
        {
            String content = renderStyleSheet(model, "cashgame_history.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "history")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    @GET
    @Path("/addPlayer.html")
    public Response addPlayer()
    {
        CashGameAction action = getCashGameAction();
        final TournamentPlayerListModel model = action.getPlayerListModel(
                        getUriBuilder(CashGameListService.class, METHOD_NAME_LIST).build(),
                        getUriBuilder(CashGameCompetitorService.class, METHOD_NAME_LIST));
        try
        {
            String content = renderStyleSheet(model, "tournament_player_add.xslt",
                            getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "players")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    @POST
    @Path("/rebuy")
    public Response rebuyPlayers(@FormParam("competitorID") List<String> identifiers, @FormParam("rebuy") String rebuy)
    {
        CashGameAction action = getCashGameAction();
        List<String> errors = Collections.emptyList();
        try
        {
            errors = runInTransaction(() -> action.rebuyPlayers(identifiers, rebuy));
        }
        catch(Exception e)
        {
            return render(e);
        }
        return redirectPlayers(errors);
    }

    private Response redirectAddPlayer()
    {
        return Response.seeOther(getUriBuilder(CashGameCompetitorService.class, "addPlayer").build(tournamentID))
                        .build();
    }

    private Response redirectPlayers()
    {
        return redirectPlayers(Collections.emptyList());
    }

    private Response redirectPlayers(List<String> errors)
    {
        return Response.seeOther(
                        getUriBuilder(CashGameCompetitorService.class, METHOD_NAME_LIST)
                                        .queryParam("errors", errors.toArray()).build(tournamentID)).build();
    }

    @POST
    @Path("/seatOpen")
    public Response seatOpenPlayers(@FormParam("competitorID") List<String> identifiers,
                    @FormParam("amount") String amount)
    {
        CashGameAction action = getCashGameAction();
        runInTransaction(() -> action.seatOpenPlayers(identifiers, amount));
        return redirectPlayers();
    }

    @POST
    @Path("/unassign")
    public Response unassignPlayers(@FormParam("competitorID") List<String> identifiers)
    {
        CashGameAction action = getCashGameAction();
        runInTransaction(() -> action.unassignPlayers(identifiers));
        return redirectPlayers();
    }

    private String renderFrame(String content, String actionName) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getUserRef());
        FrameModel frameModel = menuFactory.getCashGameFrameModel(content, "title.list." + actionName, info,
                        tournamentBORepository, tournamentID);
        return renderStyleSheet(frameModel, "tournament_frame.xslt", getXsltProcessorParameter("tournament"));
    }
}