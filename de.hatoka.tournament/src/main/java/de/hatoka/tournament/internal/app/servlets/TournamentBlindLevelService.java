package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
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
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.BlindLevelAction;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.TournamentBlindLevelModel;

@Path("/tournament/{id}")
public class TournamentBlindLevelService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;

    private AccountService accountService;
    private final MenuFactory menuFactory = new MenuFactory();

    private Response redirect;

    public TournamentBlindLevelService()
    {
        super(RESOURCE_PREFIX);
        accountService = new AccountService(this);
    }

    public TournamentBlindLevelService(AccountService accountService)
    {
        super(RESOURCE_PREFIX);
        this.accountService = accountService;
    }

    public void setAccountService(AccountService accountService)
    {
        this.accountService = accountService;
    }

    private BlindLevelAction getBlindLevelAction()
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            redirect = accountService.redirectLogin();
            return null;
        }
        return new BlindLevelAction(accountRef, tournamentID, getInstance(TournamentBusinessFactory.class));
    }

    @POST
    @Path("/actionLevelList")
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
        if (action == null)
        {
            return redirect;
        }
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
    @Path("/createLevel")
    public Response createLevel(@FormParam("duration") Integer duration, @FormParam("smallBlind") Integer smallBlind,
                    @FormParam("bigBlind") Integer bigBlind, @FormParam("ante") Integer ante)
    {
        BlindLevelAction action = getBlindLevelAction();
        if (action == null)
        {
            return redirect;
        }
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
    @Path("/deleteLevels")
    public Response deleteLevels(List<String> identifiers)
    {
        BlindLevelAction action = getBlindLevelAction();
        if (action == null)
        {
            return redirect;
        }
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
    @Path("/levels.html")
    public Response levels()
    {
        BlindLevelAction action = getBlindLevelAction();
        if (action == null)
        {
            return redirect;
        }
        final TournamentBlindLevelModel model = action.getTournamentBlindLevelModel(getUriBuilder(
                        TournamentListService.class, "list").build());
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
        return Response.seeOther(getUriBuilder(TournamentBlindLevelService.class, "levels").build(tournamentID))
                        .build();
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBORepository tournamentBORepository = getTournamentBORepository();
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, getInfo(),
                        tournamentBORepository, tournamentID), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }

    private TournamentBORepository getTournamentBORepository()
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return factory.getTournamentBORepository(accountService.getAccountRef());
    }
}