package de.hatoka.account.internal.app.servlets;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.hatoka.account.capi.business.AccountBO;
import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.internal.app.actions.AccountAction;
import de.hatoka.account.internal.app.models.AccountListModel;
import de.hatoka.common.capi.app.servlet.AbstractService;

@Path("/accounts")
public class AccountService extends AbstractService
{
    private static final String RESOURCE_PREFIX = "de/hatoka/account/internal/templates/app/";

    @Context
    private UriInfo info;

    public AccountService()
    {
        super(RESOURCE_PREFIX);
    }

    @POST
    @Path("/action")
    public Response action(@FormParam("accountID") List<String> accountIDs, @FormParam("delete") String deleteButton,
                    @FormParam("select") String selectButton)
    {
        if (isButtonPressed(selectButton) && !accountIDs.isEmpty())
        {
            return select(accountIDs.get(0));
        }
        if (isButtonPressed(deleteButton))
        {
            return delete(accountIDs);
        }
        return redirectList();
    }

    @POST
    @Path("/create")
    public Response create(@FormParam("name") String name)
    {
        UserBO currentUserBO = getCurrentUser();
        if (currentUserBO == null)
        {
            return redirectLogin();
        }
        AccountAction accountAction = new AccountAction(currentUserBO);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                accountAction.createAccountBO(name);
            }
        });
        return redirectList();
    }

    @POST
    @Path("/delete")
    public Response delete(@FormParam("accountID") final List<String> accountIDs)
    {
        UserBO currentUserBO = getCurrentUser();
        if (currentUserBO == null)
        {
            return redirectLogin();
        }
        String selectedAccountID = getCookieValue(CookieConstants.ACCOUNT_COOKIE_NAME);
        AccountAction accountAction = new AccountAction(currentUserBO);
        runInTransaction(new Runnable()
        {
            @Override
            public void run()
            {
                accountAction.deleteAccounts(accountIDs, selectedAccountID);
            }
        });
        return redirectList();
    }

    /**
     * @return user of current request (null if no user is assigned)
     */
    private UserBO getCurrentUser()
    {
        String userID = getCookieValue(CookieConstants.USER_COOKIE_NAME);
        if (userID == null)
        {
            return null;
        }
        AccountBusinessFactory accountBusinessFactory = getInstance(AccountBusinessFactory.class);
        return accountBusinessFactory.getUserBORepository().getUserBOByID(userID);
    }

    @GET
    @Path("/list.html")
    public Response list()
    {
        UserBO currentUserBO = getCurrentUser();
        if (currentUserBO == null)
        {
            return redirectLogin();
        }
        final String currentAccountID = getCookieValue(CookieConstants.ACCOUNT_COOKIE_NAME);
        final AccountListModel accounts = new AccountAction(currentUserBO).getListModel(currentAccountID);
        return renderStyleSheet(accounts, "account");
    }

    private Response redirectList(NewCookie... cookies)
    {
        return Response.seeOther(info.getBaseUriBuilder().path(AccountService.class).path(AccountService.class,"list").build()).cookie(cookies).build();
    }

    private Response redirectLogin()
    {
        return Response.seeOther(
                        info.getBaseUriBuilder().path(LoginService.class).path(LoginService.class, "printLogin").queryParam("origin", info.getRequestUri())
                                        .build()).build();
    }

    @POST
    @Path("/select")
    public Response select(@FormParam("accountID") final String accountID)
    {
        UserBO currentUserBO = getCurrentUser();
        if (currentUserBO == null)
        {
            return redirectLogin();
        }
        AccountBO accountBO = new AccountAction(currentUserBO).getAccountBO(accountID);
        if (accountBO != null)
        {
            NewCookie accountCookie = createCookie(CookieConstants.ACCOUNT_COOKIE_NAME, accountBO.getID(),
                            "hatoka account cookie");
            return redirectList(accountCookie);
        }
        return Response.seeOther(info.getRequestUriBuilder().path(AccountService.class).path(AccountService.class,"list").queryParam("msg", "accountNotExists").build()).build();
    }
}