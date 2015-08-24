package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.BlindLevelAction;
import de.hatoka.tournament.internal.app.filter.AccountRequestFilter;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.TournamentBlindLevelModel;

@Path("/tournament/{id}/levels")
public class TournamentBlindLevelService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;

    @Context
    private HttpServletRequest servletRequest;

    private final MenuFactory menuFactory = new MenuFactory();

    public TournamentBlindLevelService()
    {
        super(RESOURCE_PREFIX);
    }

    private BlindLevelAction getBlindLevelAction()
    {
        String accountRef = AccountRequestFilter.getAccountRef(servletRequest);
        return new BlindLevelAction(accountRef, tournamentID, getInstance(TournamentBusinessFactory.class));
    }

    @POST
    @Path("/actionList")
    public Response actionPlayerList(@FormParam("levelID") List<String> identifiers,
                    @FormParam("pause") String createPauseButton, @FormParam("delete") String deleteButton,
                    @FormParam("level") String createLevelButton, @FormParam("duration") Integer duration,
                    @FormParam("smallBlind") Integer smallBlind, @FormParam("bigBlind") Integer bigBlind,
                    @FormParam("ante") Integer ante)
    {
        if (isButtonPressed(createPauseButton))
        {
            return createPause(duration);
        }
        if (isButtonPressed(createLevelButton))
        {
            return createLevel(duration, smallBlind, bigBlind, ante);
        }
        if (isButtonPressed(deleteButton))
        {
            return deleteLevels(identifiers);
        }
        return redirectLevels();
    }

    @POST
    @Path("/createPause")
    public Response createPause(@FormParam("duration") Integer duration)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createPause(duration);
            }
        });
        return redirectLevels();
    }

    @POST
    @Path("/create")
    public Response createLevel(@FormParam("duration") Integer duration, @FormParam("smallBlind") Integer smallBlind,
                    @FormParam("bigBlind") Integer bigBlind, @FormParam("ante") Integer ante)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createBlindLevel(duration, smallBlind, bigBlind, ante);
            }
        });
        return redirectLevels();
    }

    @POST
    @Path("/delete")
    public Response deleteLevels(List<String> identifiers)
    {
        BlindLevelAction action = getBlindLevelAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.deleteLevels(identifiers);
            }
        });
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
            return Response.status(200).entity(renderFrame(content, "title.list.levels")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    private Response redirectLevels()
    {
        return Response.seeOther(getUriBuilder(TournamentBlindLevelService.class, METHOD_NAME_LIST).build(tournamentID))
                        .build();
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBORepository tournamentBORepository = getTournamentBORepository();
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, info,
                        tournamentBORepository, tournamentID), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }

    private TournamentBORepository getTournamentBORepository()
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return factory.getTournamentBORepository(AccountRequestFilter.getAccountRef(servletRequest));
    }
}