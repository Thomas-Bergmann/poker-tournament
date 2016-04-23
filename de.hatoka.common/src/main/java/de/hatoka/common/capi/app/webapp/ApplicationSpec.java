package de.hatoka.common.capi.app.webapp;

import javax.ws.rs.core.Application;

public class ApplicationSpec
{
    private final Class<? extends Application> applicationClass;
    private final String pathSpec;

    public ApplicationSpec(Class<? extends Application> applicationClass, String pathSpec)
    {
        this.applicationClass = applicationClass;
        this.pathSpec = pathSpec;
    }

    public Class<? extends Application> getApplicationClass()
    {
        return applicationClass;
    }

    public String getPathSpec()
    {
        return pathSpec;
    }
}
