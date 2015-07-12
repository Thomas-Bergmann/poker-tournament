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
import de.hatoka.tournament.internal.app.actions.RankAction;
import de.hatoka.tournament.internal.app.filter.AccountRequestFilter;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.RankVO;
import de.hatoka.tournament.internal.app.models.TournamentRankModel;

@Path("/tournament/{id}/ranks")
public class TournamentRankService extends AbstractService
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

    public TournamentRankService()
    {
        super(RESOURCE_PREFIX);
    }

    private String getAccountRef()
    {
        return AccountRequestFilter.getAccountRef(servletRequest);
    }

    private RankAction getRankAction()
    {
        String accountRef = getAccountRef();
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return new RankAction(accountRef, tournamentID, factory);
    }

    @POST
    @Path("/actionList")
    public Response actionPlayerList(@FormParam("rankID") List<String> identifiers, @FormParam("delete") String deleteButton, @FormParam("save") String saveButton,
                    @FormParam("firstPosition_new") String firstPosition, @FormParam("lastPosition_new") String lastPosition, @FormParam("amount_new") String amount,
                    @FormParam("percentage_new") String percentage)
    {
        if (isButtonPressed(deleteButton))
        {
            return deleteRanks(identifiers);
        }
        if (isButtonPressed(saveButton))
        {
            return createRank(firstPosition, lastPosition, amount, percentage);
        }
        return redirect(METHOD_NAME_LIST);
    }

    @POST
    @Path("/create")
    public Response createRank(@FormParam("firstPosition_new") String firstPosition, @FormParam("lastPosition_new") String lastPosition, @FormParam("amount_new") String amount,
                    @FormParam("percentage_new") String percentage)
    {
        RankAction action = getRankAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createRank(firstPosition, lastPosition, amount, percentage);
            }
        });
        return redirect(METHOD_NAME_LIST);
    }

    @GET
    @Path("/list.html")
    public Response list()
    {
        RankAction action = getRankAction();
        final TournamentRankModel model = action.getTournamentRankModel(getUriBuilder(TournamentListService.class, METHOD_NAME_LIST).build());
        model.getPrefilled().add(new RankVO("new"));
        try
        {
            String content = renderStyleSheet(model, "tournament_ranks.xslt", getXsltProcessorParameter("tournament"));
            return Response.status(200).entity(renderFrame(content, "title.list.ranks")).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    @POST
    @Path("/delete")
    public Response deleteRanks(@FormParam("rankID") List<String> identifiers)
    {
        RankAction action = getRankAction();
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.deleteRanks(identifiers);
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