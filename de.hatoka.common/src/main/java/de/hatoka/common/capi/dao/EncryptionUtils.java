package de.hatoka.common.capi.dao;

/**
 * EncryptionUtils provides methods to store data not in plain text at database
 */
public interface EncryptionUtils
{
    /**
     * The text is encrypted and can is not reproducible from encrypted string.
     * sign(text).equals(sign(text)) but there is no function text = f(sign(text)
     *
     * @param text
     * @return sign or encrypted text
     */
    public String sign(String text);
}
