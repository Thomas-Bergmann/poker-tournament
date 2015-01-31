package de.hatoka.tournament.internal.app.servlets;

import java.util.Date;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.common.capi.dao.EncryptionUtils;
import de.hatoka.tournament.capi.config.TournamentConfiguration;
import de.hatoka.tournament.internal.app.actions.TournamentListAction;
import de.hatoka.tournament.internal.app.models.TournamentListModel;

@Path("/tournaments")
public class TournamentListService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

    @Context
    private UriInfo info;

    public TournamentListService()
    {
        super(RESOURCE_PREFIX);
    }

    @POST
    @Path("/action")
    public Response action(@FormParam("tournamentID") List<String> identifiers, @FormParam("delete") String deleteButton)
    {
        if (deleteButton != null && !deleteButton.isEmpty())
        {
            return delete(identifiers);
        }
        return redirectList();
    }

    @POST
    @Path("/create")
    public Response create(@FormParam("name") String name, @FormParam("buyIn") String buyIn)
    {
        String accountRef = getAccountRef();
        if (accountRef == null)
        {
            return redirectLogin();
        }
        TournamentListAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.createTournament(name, new Date(), buyIn);
            }
        });
        return redirectList();
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam("tournamentID") final List<String> identifiers)
    {
        String accountRef = getAccountRef();
        if (accountRef == null)
        {
            return redirectLogin();
        }
        TournamentListAction action = getAction(accountRef);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                action.deleteTournaments(identifiers);
            }
        });
        return redirectList();
    }

    private String getAccountRef()
    {
        String accountID = getCookieValue(CookieConstants.ACCOUNT_ID_COOKIE_NAME);
        String accountSign = getCookieValue(CookieConstants.ACCOUNT_SIGN_COOKIE_NAME);
        return getAccountRef(accountID, accountSign);
    }

    private String getAccountRef(String accountID, String accountSign)
    {
        String secret = getInstance(TournamentConfiguration.class).getSecret();
        String expected = getInstance(EncryptionUtils.class).sign(secret, accountID);
        if (expected.equals(accountSign))
        {
            return accountID;
        }
        return null;
    }

    private TournamentListAction getAction(String accountRef)
    {
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
        String accountRef = getAccountRef();
        if (accountRef == null)
        {
            return redirectLogin();
        }
        final TournamentListModel model = getAction(accountRef).getListModel(
                        getUriBuilder(TournamentPlayerService.class, "players"));
        return renderStyleSheet(model, "tournament_list.xslt", "tournament");
    }

    @GET
    @Path("/register.html")
    public Response register( @DefaultValue("") @QueryParam("accountID")  final String accountID,
                              @DefaultValue("") @QueryParam("accountSign") final String accountSign)
    {
        if (accountID == null || accountID.isEmpty())
        {
            return Response.seeOther(
                            info.getBaseUriBuilder().uri(getInstance(TournamentConfiguration.class).getLoginURI())
                                            .queryParam("origin", info.getRequestUri()).build()).build();
        }
        if (getAccountRef(accountID, accountSign) == null)
        {
            return redirectLogin();
        }
        NewCookie accountIDCookie = createCookie(CookieConstants.ACCOUNT_ID_COOKIE_NAME, accountID, "hatoka account cookie");
        NewCookie accountSignCookie = createCookie(CookieConstants.ACCOUNT_SIGN_COOKIE_NAME, accountSign, "hatoka account cookie");
        return redirectList(accountIDCookie, accountSignCookie);
    }

    private Response redirectList(NewCookie... cookies)
    {
        return Response.seeOther(getUriBuilder(TournamentListService.class, "list").build()).cookie(cookies).build();
    }

    private Response redirectLogin()
    {
        return Response.seeOther(getUriBuilder(TournamentListService.class, "register").build()).build();
    }

}