package de.hatoka.common.capi.modules;

import java.util.Locale;
import java.util.TimeZone;

import com.google.inject.Binder;
import com.google.inject.Module;

public class LocalizationRequestModule implements Module
{
    private final Locale locale;
    private final TimeZone timeZone;

    public LocalizationRequestModule(Locale locale, TimeZone timeZone)
    {
        this.locale = locale;
        this.timeZone = timeZone;
    }

    @Override
    public void configure(Binder binder)
    {
        binder.bind(Locale.class).toProvider((com.google.inject.Provider<Locale>)() -> locale);
        binder.bind(TimeZone.class).toProvider((com.google.inject.Provider<TimeZone>)()->timeZone);
    }

}
