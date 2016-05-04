package de.hatoka.group.internal.config;

import java.net.URI;

import javax.inject.Inject;

import org.slf4j.LoggerFactory;

import de.hatoka.common.capi.configuration.SystemPropertyProvider;
import de.hatoka.group.capi.config.GroupConfiguration;

/**
 * Bean configuration for tournament
 */
public class GroupConfigurationSystemEnvImpl implements GroupConfiguration
{
    private static final String ACCOUNT_LOGIN = "ACCOUNT_LOGIN";
    private static final String ACCOUNT_SECRET = "ACCOUNT_SECRET";

    @Inject
    private SystemPropertyProvider system;

    @Override
    public URI getLoginURI()
    {
        String loginURI = system.get(ACCOUNT_LOGIN);
        try
        {
            return new URI(loginURI);
        }
        catch(Exception e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't convert string '"+loginURI+"' to URI.", e);
            return null;
        }
    }

    @Override
    public String getSecret()
    {
        return system.get(ACCOUNT_SECRET);
    }
}
