package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;

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
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.TournamentAction;
import de.hatoka.tournament.internal.app.filter.AccountRequestFilter;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.TournamentConfigurationModel;

@Path("/tournament/{id}")
public class TournamentService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    private static final String METHOD_NAME_LIST = "list";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;
    @Context
    private HttpServletRequest servletRequest;

    private final MenuFactory menuFactory = new MenuFactory();

    public TournamentService()
    {
        super(RESOURCE_PREFIX);
    }

    private String getAccountRef()
    {
        return AccountRequestFilter.getAccountRef(servletRequest);
    }

    private TournamentAction getTournamentAction()
    {
        String accountRef = getAccountRef();
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return new TournamentAction(accountRef, tournamentID, factory);
    }

    @GET
    @Path("/overview.html")
    public Response list()
    {
        TournamentAction action = getTournamentAction();
        final TournamentConfigurationModel model = action.getTournamentConfigurationModel(getUriBuilder(TournamentListService.class, METHOD_NAME_LIST).build());
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

    @POST
    @Path("/saveConfiguration")
    public Response saveConfiguration(@FormParam("name") String name, @FormParam("initialStack") Integer initialStack, @FormParam("smallestTable") Integer smallestTable,
                    @FormParam("largestTable") Integer largestTable)
    {
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
                TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getAccountRef());
                TournamentBO tournament = tournamentBORepository.getTournamentByID(tournamentID);
                tournament.setMaximumNumberOfPlayersPerTable(largestTable);
                tournament.setInitialStacksize(initialStack);
                tournament.setName(name);
            }
        });
        return redirect(METHOD_NAME_LIST);
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getAccountRef());
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, info, tournamentBORepository, tournamentID), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }
}