package de.hatoka.common.internal.app.models;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;

import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.common.capi.app.servlet.ServletConstants;

public class RequestUserRefProviderImpl implements RequestUserRefProvider
{
    @Inject
    Provider<HttpServletRequest> httpServletRequestProvider;

    @Override
    public String getUserRef()
    {
        String result = (String) httpServletRequestProvider.get().getAttribute(ServletConstants.SERVLET_ATTRIBUTE_ACCOUNT_REF);
        if (result == null)
        {
            LoggerFactory.getLogger(RequestUserRefProviderImpl.class).error("UserRef not installed to avoid illegal access to resources.");
            throw new SecurityException("Redirect to login necessary");
        }
        return result;
    }

    @Override
    public void setUserRef(String userRef)
    {
        httpServletRequestProvider.get().setAttribute(ServletConstants.SERVLET_ATTRIBUTE_ACCOUNT_REF, userRef);
    }
}
