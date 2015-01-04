package de.hatoka.jndi.account;

import java.net.URI;

/**
 * Bean configuration for tournament
 */
public class ClientConfigurationBean
{
    private URI loginURI;

    public URI getLoginURI()
    {
        return loginURI;
    }

    public void setLoginURI(URI loginURI)
    {
        this.loginURI = loginURI;
    }

}
