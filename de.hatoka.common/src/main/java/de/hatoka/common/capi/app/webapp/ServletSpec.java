package de.hatoka.common.capi.app.webapp;

import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

public class ServletSpec
{
    private final List<ApplicationSpec> applications;
    private final List<EventListener> eventListeners;

    public ServletSpec(List<ApplicationSpec> applications, List<EventListener> eventListeners)
    {
        this.applications = applications;
        this.eventListeners = eventListeners;
    }

    public ServletSpec(ApplicationSpec[] applicationSpecs)
    {
        this(Arrays.asList(applicationSpecs), Collections.emptyList());
    }

    public List<ApplicationSpec> getApplications()
    {
        return applications;
    }

    public List<EventListener> getEventListeners()
    {
        return eventListeners;
    }
}
