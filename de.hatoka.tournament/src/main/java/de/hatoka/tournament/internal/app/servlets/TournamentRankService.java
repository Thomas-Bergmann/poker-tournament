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
import de.hatoka.tournament.internal.app.actions.RankAction;
import de.hatoka.tournament.internal.app.menu.MenuFactory;
import de.hatoka.tournament.internal.app.models.RankVO;
import de.hatoka.tournament.internal.app.models.TournamentRankModel;

@Path("/tournament/{id}")
public class TournamentRankService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

    @PathParam("id")
    private String tournamentID;

    @Context
    private UriInfo info;

    private AccountService accountService;
    private final MenuFactory menuFactory = new MenuFactory();

    private Response redirect;

    public TournamentRankService()
    {
        super(RESOURCE_PREFIX);
        accountService = new AccountService(this);
    }

    public TournamentRankService(AccountService accountService)
    {
        super(RESOURCE_PREFIX);
        this.accountService = accountService;
    }

    public void setAccountService(AccountService accountService)
    {
        this.accountService = accountService;
    }

    private RankAction getRankAction()
    {
        String accountRef = accountService.getAccountRef();
        if (accountRef == null)
        {
            redirect = accountService.redirectLogin();
            return null;
        }
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        return new RankAction(accountRef, tournamentID, factory);
    }

    @POST
    @Path("/actionRankList")
    public Response actionPlayerList(@FormParam("rankID") List<String> identifiers, @FormParam("delete") String deleteButton, @FormParam("save") String saveButton,
                    @FormParam("sort") String sortButton, @FormParam("firstPosition_new") String firstPosition, @FormParam("lastPosition_new") String lastPosition,
                    @FormParam("amount_new") String amount, @FormParam("percentage_new") String percentage)
    {
        if (isButtonPressed(deleteButton))
        {
            return deleteRanks(identifiers);
        }
        if (isButtonPressed(saveButton))
        {
            return createRank(firstPosition, lastPosition, amount, percentage);
        }
        return redirectRanks();
    }

    @POST
    @Path("/createRank")
    public Response createRank(@FormParam("firstPosition_new") String firstPosition, @FormParam("lastPosition_new") String lastPosition, @FormParam("amount_new") String amount,
                    @FormParam("percentage_new") String percentage)
    {
        RankAction action = getRankAction();
        if (action == null)
        {
            return redirect;
        }
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createRank(firstPosition, lastPosition, amount, percentage);
            }
        });
        return redirectRanks();
    }

    @GET
    @Path("/ranks.html")
    public Response ranks()
    {
        RankAction action = getRankAction();
        if (action == null)
        {
            return redirect;
        }
        final TournamentRankModel model = action.getTournamentRankModel(getUriBuilder(TournamentListService.class, "list").build());
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

    private Response redirectRanks()
    {
        return Response.seeOther(getUriBuilder(TournamentRankService.class, "ranks").build(tournamentID)).build();
    }

    @POST
    @Path("/deleteRanks")
    public Response deleteRanks(@FormParam("rankID") List<String> identifiers)
    {
        RankAction action = getRankAction();
        if (action == null)
        {
            return redirect;
        }
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.deleteRanks(identifiers);
            }
        });
        return redirectRanks();
    }

    private String renderFrame(String content, String titleKey) throws IOException
    {
        TournamentBusinessFactory factory = getInstance(TournamentBusinessFactory.class);
        TournamentBORepository tournamentBORepository = factory.getTournamentBORepository(accountService.getAccountRef());
        return renderStyleSheet(menuFactory.getTournamentFrameModel(content, titleKey, getInfo(), tournamentBORepository, tournamentID), "tournament_frame.xslt",
                        getXsltProcessorParameter("tournament"));
    }
}