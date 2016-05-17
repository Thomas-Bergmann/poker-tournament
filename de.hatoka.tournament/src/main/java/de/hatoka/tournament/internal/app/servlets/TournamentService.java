package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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
import de.hatoka.tournament.internal.app.models.TournamentBigScreenModel;
import de.hatoka.tournament.internal.app.models.TournamentConfigurationModel;

@Path("/tournament/{id}")
public class TournamentService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_OVERVIEW = "list";
    public static final String METHOD_NAME_SCREEN = "screen";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;

    private final MenuFactory menuFactory = new MenuFactory();

    public TournamentService()
    {
        super(RESOURCE_PREFIX);
    }

    private TournamentAction getTournamentAction()
    {
        String accountRef = getUserRef();
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return new TournamentAction(accountRef, tournamentID, factory);
    }

    @GET
    @Path("/overview.html")
    public Response list()
    {
        TournamentAction action = getTournamentAction();
        final TournamentConfigurationModel model = action.getTournamentConfigurationModel(getUriBuilder(TournamentListService.class, METHOD_NAME_OVERVIEW).build());
        try
        {
            String content = renderStyleSheet(model, "tournament_general.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.tournament.general")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    @GET
    @Path("/screen.html")
    public Response screen()
    {
        TournamentAction action = getTournamentAction();
        final TournamentBigScreenModel model = action.getTournamentBigScreenModel(getInjector().getProvider(Date.class).get());
        try
        {
            String content = renderStyleSheet(model, "tournament_bigscreen.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderBigScreenFrame(content, "title.tournament.screen")).build();
        }
        catch(IOException e)
        {
            return render(e);
        }
    }

    @POST
    @Path("/action")
    public Response action(@FormParam("back.to.blinds") String blindsButton)
    {
        if (isButtonPressed(blindsButton))
        {
            return Response.seeOther(getUriBuilder(TournamentBlindLevelService.class, TournamentBlindLevelService.METHOD_NAME_LIST).build(tournamentID)).build();
        }
        return redirect(METHOD_NAME_OVERVIEW);
    }

    @POST
    @Path("/saveConfiguration")
    public Response saveConfiguration(@FormParam("name") String name, @FormParam("initialStack") Integer initialStack, @FormParam("smallestTable") Integer smallestTable,
                    @FormParam("largestTable") Integer largestTable, @FormParam("reBuy") BigDecimal reBuy)
    {
        runInTransaction(() -> {
            TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
            TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getUserRef());
            TournamentBO tournament = tournamentBORepository.getTournamentByID(tournamentID);
            tournament.setMaximumNumberOfPlayersPerTable(largestTable);
            tournament.setInitialStacksize(initialStack);
            tournament.setName(name);
            tournament.setReBuy(reBuy);
        });
        return redirect(METHOD_NAME_OVERVIEW, tournamentID);
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getUserRef());
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, info, tournamentBORepository, tournamentID), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }

    private String renderBigScreenFrame(String content, String titleKey) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getUserRef());
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, info, tournamentBORepository, tournamentID), "tournament_bigscreen_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }
}