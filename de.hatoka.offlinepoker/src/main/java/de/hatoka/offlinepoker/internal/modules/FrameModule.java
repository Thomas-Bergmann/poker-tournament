package de.hatoka.offlinepoker.internal.modules;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.hatoka.common.capi.app.FrameRenderer;
import de.hatoka.offlinepoker.internal.app.frame.FrameRendererImpl;

public class FrameModule implements Module
{
    @Override
    public void configure(Binder binder)
    {
        binder.bind(FrameRenderer.class).to(FrameRendererImpl.class).asEagerSingleton();
    }
}
