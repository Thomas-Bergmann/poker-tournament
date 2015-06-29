package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.TournamentAction;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.TournamentConfigurationModel;

@Path("/tournament/{id}")
public class TournamentService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;

    private AccountService accountService;
    private final MenuFactory menuFactory = new MenuFactory();

    private Response redirect;

    public TournamentService()
    {
        super(RESOURCE_PREFIX);
        accountService = new AccountService(this);
    }

    public TournamentService(AccountService accountService)
    {
        super(RESOURCE_PREFIX);
        this.accountService = accountService;
    }

    public void setAccountService(AccountService accountService)
    {
        this.accountService = accountService;
    }

    private TournamentAction getTournamentAction()
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            redirect = accountService.redirectLogin();
            return null;
        }
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return new TournamentAction(accountRef, tournamentID, factory);
    }

    @GET
    @Path("/general.html")
    public Response general()
    {
        TournamentAction action = getTournamentAction();
        if (action == null)
        {
            return redirect;
        }
        final TournamentConfigurationModel model = action.getTournamentConfigurationModel(getUriBuilder(TournamentListService.class, "list").build());
        try
        {
            String content = renderStyleSheet(model, "tournament_general.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.tournament.general")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    private Response redirectOverview()
    {
        return Response.seeOther(getUriBuilder(TournamentService.class, "general").build(tournamentID)).build();
    }

    @POST
    @Path("/saveConfiguration")
    public Response saveConfiguration(@FormParam("name") String name, @FormParam("initialStack") Integer initialStack, @FormParam("smallestTable") Integer smallestTable,
                    @FormParam("largestTable") Integer largestTable)
    {
        TournamentAction action = getTournamentAction();
        if (action == null)
        {
            return redirect;
        }
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
                TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountService.getAccountRef());
                TournamentBO tournament = tournamentBORepository.getTournamentByID(tournamentID);
                tournament.setMininumNumberOfPlayersPerTable(smallestTable);
                tournament.setMaximumNumberOfPlayersPerTable(largestTable);
                tournament.setInitialStacksize(initialStack);
                tournament.setName(name);
            }
        });
        return redirectOverview();
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountService.getAccountRef());
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, getInfo(), tournamentBORepository, tournamentID), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }
}