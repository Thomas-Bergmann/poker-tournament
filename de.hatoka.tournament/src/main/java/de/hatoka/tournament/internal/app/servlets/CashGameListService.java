package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import de.hatoka.common.capi.app.FrameRenderer;
import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.internal.app.actions.TournamentListAction;
import de.hatoka.tournament.internal.app.models.TournamentListModel;

@Path("/cashgames")
public class CashGameListService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";
    public static final String METHOD_NAME_ADD = "add";

    public CashGameListService()
    {
        super(RESOURCE_PREFIX);
    }

    @POST
    @Path("/action")
    public Response action(@FormParam("tournamentID") List<String> identifiers, @FormParam("delete") String deleteButton)
    {
        if (isButtonPressed(deleteButton))
        {
            return delete(identifiers);
        }
        return redirect(METHOD_NAME_LIST);
    }

    @POST
    @Path("/create")
    public Response create(@FormParam("buyIn") String buyIn)
    {
        TournamentListAction action = getAction();
        try
        {
            runInTransaction(() -> action.createCashGame(new Date(), buyIn));
        }
        catch(Exception e)
        {
            return render(e);
        }
        return redirect(METHOD_NAME_LIST);
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam("tournamentID") final List<String> identifiers)
    {
        TournamentListAction action = getAction();
        runInTransaction(() -> action.deleteCashGames(identifiers));
        return redirect(METHOD_NAME_LIST);
    }

    private TournamentListAction getAction()
    {
        TournamentListAction action = new TournamentListAction();
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
        final TournamentListModel model = getAction().getCashGameListModel(getUriBuilderPlayers());
        try
        {
            String content = renderStyleSheet(model, "tournament_list.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "list")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    private UriBuilder getUriBuilderPlayers()
    {
        return getUriBuilder(CashGameCompetitorService.class, METHOD_NAME_LIST);
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
        final TournamentListModel model = getAction().getCashGameListModel(getUriBuilderPlayers());
        try
        {
            String content = renderStyleSheet(model, "cashgame_add.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "create")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    private String renderFrame(String content, String subItem)
    {
        return getInstance(FrameRenderer.class).renderFame(content, "cashgames", subItem);
    }
}