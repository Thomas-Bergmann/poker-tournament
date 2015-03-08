package de.hatoka.common.capi.app.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.persistence.EntityTransaction;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.common.internal.app.models.ErrorModel;

public class AbstractService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);
    private static final Locale DEFAULT_LOCALE = Locale.US;
    private static final XSLTRenderer RENDERER = new XSLTRenderer();

    private final String resourcePrefix;

    @Context
    private Application application;

    @Context
    private UriInfo info;

    @Context
    private HttpHeaders headers;

    public AbstractService(String resourcePrefix)
    {
        this.resourcePrefix = resourcePrefix;
    }

    public NewCookie createCookie(String key, String value, String comment)
    {
        return new NewCookie(key, value, "/", null, NewCookie.DEFAULT_VERSION, comment, NewCookie.DEFAULT_MAX_AGE,
                        null, false, true);
    }

    public String getCookieValue(String key)
    {
        Cookie cookie = headers.getCookies().get(key);
        if (cookie == null)
        {
            return null;
        }
        String result = cookie.getValue();
        if (result != null && result.isEmpty())
        {
            result = null;
        }
        return result;
    }

    protected Injector getInjector()
    {
        Injector injector = (Injector)application.getProperties().get(ServletConstants.PROPERTY_INJECTOR);
        return injector;
    }

    public <T> T getInstance(Class<T> classOfT)
    {
        return getInjector().getInstance(classOfT);
    }

    protected Locale getLocale()
    {
        return DEFAULT_LOCALE;
    }

    private EntityTransaction getTransaction()
    {
        return getInstance(TransactionProvider.class).get();
    }

    public UriBuilder getUriBuilder(Class<?> resource, String methodName)
    {
        return getInfo().getBaseUriBuilder().path(resource).path(resource, methodName);
    }

    protected String getUUID()
    {
        return getInstance(UUIDGenerator.class).generate();
    }

    protected void injectMembers(Object action)
    {
        getInjector().injectMembers(action);
    }

    protected Response render(int status, Throwable exception)
    {
        String errorID = getUUID();
        LOGGER.error("Error: " + errorID, exception);
        StringWriter writer = new StringWriter();
        try
        {
            RENDERER.render(writer, new ErrorModel(errorID, exception), "de/hatoka/common/capi/app/xslt/error.xslt",
                            getXsltProcessorParameter(null));
        }
        catch(IOException e)
        {
            LOGGER.error("Can't create error page", e);
        }
        return Response.status(status).entity(writer.toString()).build();
    }

    /**
     *
     * @param jaxbModel
     * @param stylesheet xslt file without resourcePrefix
     * @param resourceName
     *            for localization without resourcePrefix
     * @return
     */
    protected Response renderResponseWithStylesheet(Object jaxbModel, String stylesheet, String resourceName, NewCookie... cookies)
    {
        try
        {
            return Response.status(200).cookie(cookies).entity(renderStyleSheet(jaxbModel, stylesheet, getXsltProcessorParameter(resourceName))).build();
        }
        catch(IOException e)
        {
            return render(500, e);
        }
    }

    protected String renderStyleSheet(Object jaxbModel, String stylesheet, Map<String, Object> xsltProcessorParameter) throws IOException
    {
        return RENDERER.render(jaxbModel, resourcePrefix + stylesheet, xsltProcessorParameter);
    }


    /**
     * @param localizationResource without resourcePrefix
     * @return parameter for xslt processor
     */
    protected Map<String, Object> getXsltProcessorParameter(String localizationResource)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("uriInfo", getInfo());
        if (localizationResource != null)
        {
            result.put("localizer", new ResourceLocalizer(new LocalizationBundle(resourcePrefix
                            + localizationResource, getLocale())));
        }
        return result;
    }

    protected <T> T runInTransaction(Callable<T> callable) throws Exception
    {
        T result = null;
        EntityTransaction transaction = getTransaction();
        try
        {
            transaction.begin();
            result = callable.call();
            transaction.commit();
        }
        finally
        {
            if (transaction.isActive())
            {
                transaction.rollback();
            }
        }
        return result;
    }

    /**
     * Runs the given parameter inside of a transaction
     *
     * @param runnable
     */
    protected void runInTransaction(Runnable runnable)
    {
        EntityTransaction transaction = getTransaction();
        try
        {
            transaction.begin();
            runnable.run();
            transaction.commit();
        }
        finally
        {
            if (transaction.isActive())
            {
                transaction.rollback();
            }
        }
    }

    /**
     * @param button
     *            value
     * @return true if button value was transmitted
     */
    protected boolean isButtonPressed(String button)
    {
        return button != null;
    }

    public UriInfo getInfo()
    {
        return info;
    }
}
