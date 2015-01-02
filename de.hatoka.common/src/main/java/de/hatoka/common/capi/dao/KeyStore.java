package de.hatoka.common.capi.dao;

import java.io.IOException;
import java.io.Serializable;

/**
 * Designed to store data in a remote "cache" map.
 */
public interface KeyStore
{
    <T> T get(String key) throws IOException;

    void put(String key, Serializable object) throws IOException;
}
