package de.hatoka.common.capi.app.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
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

import de.hatoka.common.capi.app.xslt.Processor;
import de.hatoka.common.capi.dao.TransactionProvider;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.common.internal.app.models.ErrorModel;

public class AbstractService
{
    private static final String ERROR_RESOURCE_PREFIX = "de/hatoka/common/capi/app/xslt/";
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);
    private static final Locale DEFAULT_LOCALE = Locale.US;

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
        return info.getBaseUriBuilder().path(resource).path(resource, methodName);
    }

    protected String getUUID()
    {
        return getInstance(UUIDGenerator.class).generate();
    }

    protected Processor getXsltErrorProcessor()
    {
        return new Processor(ERROR_RESOURCE_PREFIX, new ResourceLocalizer(new LocalizationBundle(ERROR_RESOURCE_PREFIX
                        + "error", getLocale())));
    }

    protected Processor getXsltProcessor(String resourceName)
    {
        return new Processor(resourcePrefix, new ResourceLocalizer(new LocalizationBundle(
                        resourcePrefix + resourceName, getLocale())));
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
            getXsltErrorProcessor().process(new ErrorModel(errorID, exception), "error.xslt", writer, info);
        }
        catch(IOException e)
        {
            LOGGER.error("Can't create error page", e);
        }
        return Response.status(200).entity(writer.toString()).build();
    }

    /**
     * Render stylesheet (with resourceName for localization is equal to
     * stylesheet).
     *
     * @param object
     * @param resourceName
     * @return response
     */
    protected Response renderStyleSheet(Object object, String resourceName)
    {
        return renderStyleSheet(object, resourceName + ".xslt", resourceName);
    }

    /**
     * Render stylesheet (with resourceName for localization is equal to
     * stylesheet).
     *
     * @param object
     * @param stylesheet and resourcename
     * @param cookies
     * @return
     */
    protected Response renderStyleSheet(Object object, String stylesheet, NewCookie... cookies)
    {
        StringWriter writer = new StringWriter();
        try
        {
            getXsltProcessor(stylesheet).process(object, stylesheet + ".xslt", writer, info);
        }
        catch(IOException e)
        {
            return render(500, e);
        }
        return Response.status(200).cookie(cookies).entity(writer.toString()).build();
    }

    /**
     *
     * @param object
     * @param stylesheet xslt file
     * @param resourceName  for localization
     * @return
     */
    protected Response renderStyleSheet(Object object, String stylesheet, String resourceName)
    {
        StringWriter writer = new StringWriter();
        try
        {
            getXsltProcessor(resourceName).process(object, stylesheet, writer, info);
        }
        catch(IOException e)
        {
            return render(500, e);
        }
        return Response.status(200).entity(writer.toString()).build();
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
}