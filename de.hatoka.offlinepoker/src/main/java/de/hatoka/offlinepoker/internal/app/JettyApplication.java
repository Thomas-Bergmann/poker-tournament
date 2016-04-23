package de.hatoka.offlinepoker.internal.app;

import java.util.EventListener;
import java.util.List;

import javax.ws.rs.core.Application;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.LoggerFactory;

import de.hatoka.common.capi.app.webapp.ApplicationSpec;
import de.hatoka.common.capi.app.webapp.ServletSpec;

public class JettyApplication
{
    private final ServletSpec servletSpec;
    private final int port;
    private Server server;

    /**
     * Provides a running jetty server with given application at root path
     *
     * @param applicationClass
     * @param port
     */
    public JettyApplication(int port, Class<? extends Application> applicationClass)
    {
        this(port, applicationClass, "/*");
    }

    /**
     * Provides a running jetty server with given application at the given path.
     *
     * @param applicationClass
     * @param pathSpec
     *            path specification (like "/*" or "/serviceName/*")
     * @param port
     */
    public JettyApplication(int port, Class<? extends Application> applicationClass, String pathSpec)
    {
        this(port, new ApplicationSpec(applicationClass, pathSpec));
    }

    /**
     * Create jetty server with multiple applications.
     *
     * @param applicationSpecs
     * @param port
     */
    public JettyApplication(int port, ApplicationSpec... applicationSpecs)
    {
        this(port, new ServletSpec(applicationSpecs));
    }

    public JettyApplication(int port, List<ApplicationSpec> applicationSpecs, List<EventListener> listener)
    {
        this(port, new ServletSpec(applicationSpecs, listener));
    }

    public JettyApplication(int port, ServletSpec servletSpec)
    {
        this.servletSpec = servletSpec;
        this.port = port;
    }

    /**
     * Creates the jetty instance and loads the given application class
     *
     * @throws Exception
     */
    public void start() throws Exception
    {
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        int i = 1;
        for (ApplicationSpec spec : servletSpec.getApplications())
        {
            ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class,
                            spec.getPathSpec());
            jerseyServlet.setInitOrder(i++);
            jerseyServlet.setInitParameter("javax.ws.rs.Application", spec.getApplicationClass().getCanonicalName());
            jerseyServlet.setInitParameter("jersey.config.server.provider.packages",
                            "com.jersey.jaxb,com.fasterxml.jackson.jaxrs.json");
        }
        servletSpec.getEventListeners().forEach(context::addEventListener);
        server.start();
    }

    /**
     * Shutdown the jetty server after test case execution.
     */
    public void stop()
    {
        try
        {
            server.stop();
        }
        catch(Exception e)
        {
            LoggerFactory.getLogger(getClass()).error("Error during stopping the jetty server.", e);
        }
        try
        {
            server.destroy();
        }
        catch(Exception e)
        {
            LoggerFactory.getLogger(getClass()).error("Error during destroying the jetty server.", e);
        }
    }
}
