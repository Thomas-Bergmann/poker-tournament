package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import de.hatoka.common.capi.app.FrameRenderer;
import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.BlindLevelAction;
import de.hatoka.tournament.internal.app.models.TournamentBlindLevelModel;

@Path("/tournament/{id}/levels")
public class TournamentBlindLevelService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";

    @PathParam("id")
    private String tournamentID;

    public TournamentBlindLevelService()
    {
        super(RESOURCE_PREFIX);
    }

    private BlindLevelAction getBlindLevelAction()
    {
        return new BlindLevelAction(getUserRef(), tournamentID, getInstance(TournamentBusinessFactory.class));
    }

    @POST
    @Path("/actionList")
    public Response actionPlayerList(@FormParam("levelID") List<String> identifiers,
                    @FormParam("pause") String createPauseButton, @FormParam("delete") String deleteButton,
                    @FormParam("enableReBuy") String enableReBuy, @FormParam("disableReBuy") String disabledReBuy,
                    @FormParam("create") String createLevelButton, @FormParam("start") String startLevelButton,
                    @FormParam("duration") Integer duration,
                    @FormParam("smallBlind") Integer smallBlind, @FormParam("bigBlind") Integer bigBlind,
                    @FormParam("ante") Integer ante)
    {
        if (isButtonPressed(createPauseButton))
        {
            return createPause(duration);
        }
        if (isButtonPressed(startLevelButton))
        {
            return startLevel(identifiers);
        }
        if (isButtonPressed(createLevelButton))
        {
            return createLevel(duration, smallBlind, bigBlind, ante);
        }
        if (isButtonPressed(deleteButton))
        {
            return deleteLevels(identifiers);
        }
        if (isButtonPressed(enableReBuy))
        {
            return enableReBuy(identifiers);
        }
        if (isButtonPressed(disabledReBuy))
        {
            return disableReBuy(identifiers);
        }
        return redirectLevels();
    }

    @POST
    @Path("/createPause")
    public Response createPause(@FormParam("duration") Integer duration)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(() -> action.createPause(duration));
        return redirectLevels();
    }

    @POST
    @Path("/create")
    public Response createLevel(@FormParam("duration") Integer duration, @FormParam("smallBlind") Integer smallBlind,
                    @FormParam("bigBlind") Integer bigBlind, @FormParam("ante") Integer ante)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(() -> action.createBlindLevel(duration, smallBlind, bigBlind, ante));
        return redirectLevels();
    }

    @POST
    @Path("/delete")
    public Response deleteLevels(List<String> identifiers)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(() -> action.deleteLevels(identifiers));
        return redirectLevels();
    }

    @POST
    @Path("/start")
    public Response startLevel(List<String> identifiers)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(() -> action.startLevel(identifiers.get(0)));
        return redirectLevels();
    }

    @POST
    @Path("/disableReBuy")
    public Response disableReBuy(List<String> identifiers)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(() -> action.disableReBuy(identifiers));
        return redirectLevels();
    }

    @POST
    @Path("/enableReBuy")
    public Response enableReBuy(List<String> identifiers)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(() -> action.enableReBuy(identifiers));
        return redirectLevels();
    }

    @GET
    @Path("/list.html")
    public Response list()
    {
        BlindLevelAction action = getBlindLevelAction();
        final TournamentBlindLevelModel model = action.getTournamentBlindLevelModel(getUriBuilder(
                        TournamentListService.class, METHOD_NAME_LIST).build());
        try
        {
            String content = renderStyleSheet(model, "tournament_blinds.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "levels")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    private Response redirectLevels()
    {
        return Response.seeOther(getUriBuilder(TournamentBlindLevelService.class, METHOD_NAME_LIST).build(tournamentID))
                        .build();
    }

    private String renderFrame(String content, String subItem)
    {
        return getInstance(FrameRenderer.class).renderFame(content, "tournament", tournamentID, subItem);
    }
}