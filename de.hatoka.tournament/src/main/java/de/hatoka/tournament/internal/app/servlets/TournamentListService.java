package de.hatoka.tournament.internal.app.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.actions.TournamentListAction;
import de.hatoka.tournament.internal.app.filter.AccountRequestFilter;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.TournamentListModel;

@Path("/tournaments")
public class TournamentListService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    public static final String METHOD_NAME_LIST = "list";
    public static final String METHOD_NAME_ADD = "add";

    @Context
    private UriInfo info;
    @Context
    private HttpServletRequest servletRequest;

    private final MenuFactory menuFactory = new MenuFactory();

    public TournamentListService()
    {
        super(RESOURCE_PREFIX);
    }
    private String getAccountRef()
    {
        return AccountRequestFilter.getAccountRef(servletRequest);
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
    public Response create(@FormParam("name") String name, @FormParam("buyIn") String buyIn)
    {
        TournamentListAction action = getAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createTournament(name, new Date(), buyIn);
            }
        });
        return redirect(METHOD_NAME_LIST);
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam("tournamentID") final List<String> identifiers)
    {
        TournamentListAction action = getAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.deleteTournaments(identifiers);
            }
        });
        return redirect(METHOD_NAME_LIST);
    }

    private TournamentListAction getAction()
    {
        String accountRef = getAccountRef();
        TournamentListAction action = new TournamentListAction(accountRef);
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
        final TournamentListModel model = getAction().getTournamentListModel(
                        getUriBuilder(TournamentCompetitorService.class, METHOD_NAME_LIST));
        try
        {
            String content = renderStyleSheet(model, "tournament_list.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.list.tournament")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
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
        final TournamentListModel model = getAction().getTournamentListModel(
                        getUriBuilder(TournamentCompetitorService.class, METHOD_NAME_LIST));
        try
        {
            String content = renderStyleSheet(model, "tournament_add.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.create.tournament")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        final String accountRef = AccountRequestFilter.getAccountRef(servletRequest);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountRef);
        PlayerBORepository playerBORepository = factory.getPlayerBORepository(accountRef);
        return renderStyleSheet(menuFactory.getMainFrameModel(content, titleKey, info, tournamentBORepository, playerBORepository, "tournaments"), "tournament_frame.xslt", getXsltProcessorParameter("tournament"));
    }
}