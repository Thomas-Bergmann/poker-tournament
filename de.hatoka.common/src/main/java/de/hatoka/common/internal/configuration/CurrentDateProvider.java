package de.hatoka.common.internal.configuration;

import java.util.Date;

import org.springframework.stereotype.Component;

import de.hatoka.common.capi.configuration.DateProvider;

@Component
public class CurrentDateProvider implements DateProvider
{
    @Override
    public Date get()
    {
        return new Date();
    }

}
