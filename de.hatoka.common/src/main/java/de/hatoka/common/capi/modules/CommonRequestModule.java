package de.hatoka.common.capi.modules;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.common.internal.app.models.RequestUserRefProviderImpl;

public class CommonRequestModule implements Module
{
    private final Application application;
    private final HttpServletRequest servletRequest;

    public CommonRequestModule(Application application, HttpServletRequest servletRequest)
    {
        this.application = application;
        this.servletRequest = servletRequest;
    }

    @Override
    public void configure(Binder binder)
    {
        binder.bind(Application.class).toProvider((com.google.inject.Provider<Application>)() -> application);
        binder.bind(HttpServletRequest.class).toProvider((com.google.inject.Provider<HttpServletRequest>)()->servletRequest);
        binder.bind(RequestUserRefProvider.class).to(RequestUserRefProviderImpl.class).asEagerSingleton();
    }

}
