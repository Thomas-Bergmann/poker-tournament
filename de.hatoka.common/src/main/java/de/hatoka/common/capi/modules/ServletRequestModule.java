package de.hatoka.common.capi.modules;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.common.internal.app.models.RequestUserRefProviderImpl;

public class ServletRequestModule implements Module
{
    private final Application application;
    private final HttpServletRequest servletRequest;
    private final UriInfo uriInfo;

    public ServletRequestModule(Application application, HttpServletRequest servletRequest, UriInfo uriInfo)
    {
        this.application = application;
        this.servletRequest = servletRequest;
        this.uriInfo = uriInfo;
    }

    @Override
    public void configure(Binder binder)
    {
        binder.bind(Application.class).toProvider((com.google.inject.Provider<Application>)() -> application);
        binder.bind(UriInfo.class).toProvider((com.google.inject.Provider<UriInfo>)() -> uriInfo);
        binder.bind(HttpServletRequest.class).toProvider((com.google.inject.Provider<HttpServletRequest>)()->servletRequest);
        binder.bind(RequestUserRefProvider.class).to(RequestUserRefProviderImpl.class).asEagerSingleton();
    }

}
