package de.hatoka.tournament.internal.app.servlets;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import de.hatoka.common.capi.app.servlet.AbstractService;
import de.hatoka.common.capi.dao.EncryptionUtils;
import de.hatoka.tournament.capi.config.TournamentConfiguration;

public class AccountService
{
    private final AbstractService service;

    public AccountService(AbstractService service)
    {
        this.service = service;
    }
    public String getAccountRef()
    {
        String accountID = service.getCookieValue(CookieConstants.ACCOUNT_ID_COOKIE_NAME);
        String accountSign = service.getCookieValue(CookieConstants.ACCOUNT_SIGN_COOKIE_NAME);
        return getAccountRef(accountID, accountSign);
    }

    public String getAccountRef(String accountID, String accountSign)
    {
        String secret = service.getInstance(TournamentConfiguration.class).getSecret();
        String expected = service.getInstance(EncryptionUtils.class).sign(secret, accountID);
        if (expected.equals(accountSign))
        {
            return accountID;
        }
        return null;
    }


    public List<NewCookie> getCookies(String accountID, String accountSign)
    {
        List<NewCookie> result = new ArrayList<>();
        result.add(service.createCookie(CookieConstants.ACCOUNT_ID_COOKIE_NAME, accountID, "hatoka account cookie"));
        result.add(service.createCookie(CookieConstants.ACCOUNT_SIGN_COOKIE_NAME, accountSign, "hatoka account cookie"));
        return result;
    }


    public Response redirectLogin()
    {
        return Response.seeOther(service.getUriBuilder(TournamentListService.class, "register").build()).build();
    }

}
