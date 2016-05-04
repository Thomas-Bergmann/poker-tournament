package de.hatoka.group.capi.config;

import java.net.URI;

public interface GroupConfiguration
{
    URI getLoginURI();

    String getSecret();
}
