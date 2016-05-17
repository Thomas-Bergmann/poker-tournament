package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.PlayerAction;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.PlayerListModel;

@Path("/players")
public class PlayerListService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";

    @Context
    private UriInfo info;

    private final MenuFactory menuFactory = new MenuFactory();

    public PlayerListService()
    {
        super(RESOURCE_PREFIX);
    }

    private PlayerAction getAction()
    {
        String accountRef = getUserRef();
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return new PlayerAction(accountRef, factory);
    }

    @GET
    @Path("/list.html")
    public Response list()
    {
        PlayerAction action = getAction();
        final PlayerListModel model = action.getPlayerListModel();
        try
        {
            String content = renderStyleSheet(model, "player_list.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.list.players")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    @POST
    @Path("/action")
    public Response action(@FormParam("playerID") List<String> identifiers, @FormParam("delete") String deleteButton)
    {
        if (isButtonPressed(deleteButton))
        {
            return delete(identifiers);
        }
        return redirect(METHOD_NAME_LIST);
    }

    @DELETE
    public Response delete(@FormParam("playerID") final List<String> identifiers)
    {
        PlayerAction action = getAction();
        runInTransaction(() -> action.deletePlayers(identifiers));
        return redirect(METHOD_NAME_LIST);
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        return renderStyleSheet(menuFactory.getPlayerFrameModel(content, titleKey, info), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }
}