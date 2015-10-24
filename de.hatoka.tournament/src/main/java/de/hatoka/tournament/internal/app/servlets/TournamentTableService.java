package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
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

import de.hatoka.common.capi.app.model.MessageVO;
import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.TableAction;
import de.hatoka.tournament.internal.app.actions.TableAction.TournamentTableURIBuilder;
import de.hatoka.tournament.internal.app.filter.AccountRequestFilter;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.models.TournamentTableModel;

@Path("/tournament/{tournamentID}/tables")
public class TournamentTableService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";
    protected static final String METHOD_NAME_SEATOPEN = "seatOpen";
    protected static final String METHOD_NAME_REBUY = "rebuy";

    @PathParam("tournamentID")
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

    private TableAction getTableAction()
    {
        String accountRef = getAccountRef();
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return new TableAction(accountRef, tournamentID, factory);
    }

    private Response redirect()
    {
        return redirect(METHOD_NAME_LIST, tournamentID);
    }

    @GET
    @Path("/list.html")
    public Response list()
    {
        return list(Collections.emptyList());
    }

    private Response list(List<MessageVO> messages)
    {
        TableAction action = getTableAction();
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
            return Response.status(200).entity(renderFrame(content, "title.tournament.tables", messages)).build();
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
        return redirect();
    }

    @POST
    @Path("/{competitorID}/rebuy")
    public Response rebuy(@PathParam("competitorID") String competitorID)
    {
        TableAction action = getTableAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.rebuyCompetitor(competitorID);
            }
        });
        return redirect();
    }

    @POST
    @Path("/{competitorID}/seatOpen")
    public Response seatOpen(@PathParam("competitorID") String competitorID)
    {
        final List<MessageVO> messages = new ArrayList<MessageVO>();
        TableAction action = getTableAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                messages.addAll(action.seatOpenCompetitor(competitorID));
            }
        });
        return list(messages);
    }

    @POST
    @Path("/action")
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
        return redirect();
    }

    private String renderFrame(String content, String titleKey, List<MessageVO> messages) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(getAccountRef());
        FrameModel tournamentFrameModel = menuFactory.getTournamentFrameModel(content, titleKey, info, tournamentBORepository, tournamentID);
        tournamentFrameModel.addMessages(messages);
        return renderStyleSheet(tournamentFrameModel, "tournament_frame.xslt", getXsltProcessorParameter("tournament"));
    }
}