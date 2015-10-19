package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.net.URI;

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
import de.hatoka.tournament.internal.app.actions.TournamentAction.TournamentTableURIBuilder;
import de.hatoka.tournament.internal.app.filter.AccountRequestFilter;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.TournamentTableModel;

@Path("/tournament/{id}/tables")
public class TournamentTableService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";
    protected static final String METHOD_NAME_SEATOPEN = "seatOpen";
    protected static final String METHOD_NAME_REBUY = "rebuy";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;
    @Context
    private HttpServletRequest servletRequest;

    private final MenuFactory menuFactory = new MenuFactory();

    public TournamentTableService()
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
    @Path("/list.html")
    public Response list()
    {
        TournamentAction action = getTournamentAction();
        final TournamentTableModel model = action.getTournamentTableModel(getUriBuilder(TournamentListService.class, METHOD_NAME_LIST).build(), new TournamentTableURIBuilder()
        {
            @Override
            public URI getSeatOpenURI(String competitorID)
            {
                return getUriBuilder(TournamentTableService.class, TournamentTableService.METHOD_NAME_SEATOPEN).build(tournamentID, competitorID);
            }

            @Override
            public URI getRebuyURI(String competitorID)
            {
                return getUriBuilder(TournamentTableService.class, TournamentTableService.METHOD_NAME_REBUY).build(tournamentID, competitorID);
            }
        });
        try
        {
            String content = renderStyleSheet(model, "tournament_tables.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.tournament.tables")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    @POST
    @Path("/{competitorID}/action")
    public Response action(@PathParam("competitorID") String competitorID, @FormParam("rebuy") String rebuyButton, @FormParam("seatOpen") String seatOpenButton)
    {
        if (isButtonPressed(rebuyButton))
        {
            return rebuy(competitorID);
        }
        if (isButtonPressed(seatOpenButton))
        {
            return seatOpen(competitorID);
        }
        return redirect(METHOD_NAME_LIST);
    }

    @POST
    @Path("/{competitorID}/rebuy")
    public Response rebuy(@PathParam("competitorID") String competitorID)
    {
        TournamentAction action = getTournamentAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.rebuyCompetitor(competitorID);
            }
        });
        return redirect(METHOD_NAME_LIST, tournamentID);
    }

    @POST
    @Path("/{competitorID}/seatOpen")
    public Response seatOpen(@PathParam("competitorID") String competitorID)
    {
        TournamentAction action = getTournamentAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.seatOpenCompetitor(competitorID);
            }
        });
        return redirect(METHOD_NAME_LIST, tournamentID);
    }

    @POST
    @Path("/actionTables")
    public Response assignTables()
    {
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
                TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getAccountRef());
                TournamentBO tournament = tournamentBORepository.getTournamentByID(tournamentID);
                tournament.placePlayersAtTables();
            }
        });
        return redirect(METHOD_NAME_LIST, tournamentID);
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getAccountRef());
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, info, tournamentBORepository, tournamentID), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }
}