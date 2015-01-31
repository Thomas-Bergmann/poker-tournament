package de.hatoka.jndi.account;

import java.net.URI;

/**
 * Bean configuration for tournament
 */
public class ClientConfigurationBean
{
    private URI loginURI;
    private String secret;
    private String dateFormat;

    public URI getLoginURI()
    {
        return loginURI;
    }

    public void setLoginURI(URI loginURI)
    {
        this.loginURI = loginURI;
    }

    public String getSecret()
    {
        return secret;
    }

    public void setSecret(String secret)
    {
        this.secret = secret;
    }

    public String getDateFormat()
    {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat)
    {
        this.dateFormat = dateFormat;
    }

}
