package de.hatoka.tournament.internal.app.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import de.hatoka.common.capi.app.servlet.ServletConstants;
import de.hatoka.common.capi.dao.EncryptionUtils;
import de.hatoka.tournament.capi.config.TournamentConfiguration;

/**
 * Filter will avoid access to servlets in case the account is not set.
 */
public class AccountRequestFilter implements ContainerRequestFilter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRequestFilter.class);

    @Context
    private HttpServletRequest servletRequest;

    @Context
    private Application application;

    @Context
    private UriInfo info;

    private static String getCookie(Map<String, javax.ws.rs.core.Cookie> cookies, String cookieName)
    {
        Cookie cookie = cookies.get(cookieName);
        return cookie == null ? null : cookie.getValue();
    }

    private NewCookie[] createCookies(String accountID, String accountSign)
    {
        NewCookie[] result = new NewCookie[2];
        result[0] = createCookie(ServletConstants.USER_ID_COOKIE_NAME, accountID, "hatoka account cookie");
        result[1] = createCookie(ServletConstants.USER_SIGN_COOKIE_NAME, accountSign, "hatoka account cookie");
        return result;
    }

    private static NewCookie createCookie(String key, String value, String comment)
    {
        return new NewCookie(key, value, "/", null, NewCookie.DEFAULT_VERSION, comment, NewCookie.DEFAULT_MAX_AGE, null, false, true);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException
    {
        if (validateCookies(requestContext))
        {
            return;
        }
        NewCookie[] cookies = validateQueryParams(requestContext);
        if(cookies.length != 0)
        {
            requestContext.abortWith(Response.seeOther(info.getRequestUri()).cookie(cookies).build());
            return;
        }
        // account resolving not successful, on GET request redirect to login
        if (requestContext.getMethod().equals(HttpMethod.GET))
        {
            requestContext.abortWith(Response.seeOther(
                            info.getBaseUriBuilder().uri(getInstance(TournamentConfiguration.class).getLoginURI()).queryParam("origin", info.getRequestUri()).build()).build());
        }
        else
        {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
        }
    }

    /**
     * Validates cookies for account registration
     *
     * @param requestContext
     * @return true to abort further tests
     */
    private boolean validateCookies(ContainerRequestContext requestContext)
    {
        if (requestContext.getUriInfo().getPath().contains("login/"))
        {
            return true;
        }
        Map<String, javax.ws.rs.core.Cookie> cookies = requestContext.getCookies();
        String accountID = getCookie(cookies, ServletConstants.USER_ID_COOKIE_NAME);
        if (accountID == null)
        {
            return false;
        }
        String accountSign = getCookie(cookies, ServletConstants.USER_SIGN_COOKIE_NAME);
        if (validateAccount(accountID, accountSign))
        {
            LOGGER.trace("Access granted to account '" + accountID + "'");
            servletRequest.setAttribute(ServletConstants.SERVLET_ATTRIBUTE_ACCOUNT_REF, accountID);
        }
        else
        {
            LOGGER.warn("Wrong access to account '" + accountID + "': with wrong signature '" + accountSign + "'");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User cannot access the resource.").build());
        }
        return true;
    }

    public NewCookie[] validateQueryParams(ContainerRequestContext requestContext)
    {
        final String accountID = info.getQueryParameters().getFirst("accountID");
        final String accountSign = info.getQueryParameters().getFirst("accountSign");
        if (accountID == null || accountID.isEmpty() || !validateAccount(accountID, accountSign))
        {
            return new NewCookie[0];
        }
        return createCookies(accountID, accountSign);
    }

    private Injector getInjector()
    {
        return (Injector)application.getProperties().get(ServletConstants.PROPERTY_INJECTOR);
    }

    private <T> T getInstance(Class<T> classOfT)
    {
        return getInjector().getInstance(classOfT);
    }

    private boolean validateAccount(String accountID, String accountSign)
    {
        String secret = getInstance(TournamentConfiguration.class).getSecret();
        String expected = getInstance(EncryptionUtils.class).sign(secret, accountID);
        return expected.equals(accountSign);
    }

    public static String getAccountRef(HttpServletRequest servletRequest)
    {
        String result = (String) servletRequest.getAttribute(ServletConstants.SERVLET_ATTRIBUTE_ACCOUNT_REF);
        if (result == null)
        {
            LoggerFactory.getLogger(AccountRequestFilter.class).error("AccountRequestFilter not installed to avoid illegal access to resources.");
            throw new SecurityException("Redirect to login necessary");
        }
        return result;
    }

}