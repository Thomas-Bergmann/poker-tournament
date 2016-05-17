package de.hatoka.common.internal.dao;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.slf4j.LoggerFactory;

import de.hatoka.common.capi.dao.EncryptionUtils;

public class EncryptionUtilsImpl implements EncryptionUtils
{
    private static String encrypt(String text, String algo) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance(algo);
        md.update(text.getBytes("UTF-8"));
        byte raw[] = md.digest();
        return new String(Base64.getEncoder().encode(raw));
    }

    @Override
    public String sign(String text)
    {
        try
        {
            return encrypt(text, "SHA");
        }
        catch(NoSuchAlgorithmException | UnsupportedEncodingException e)
        {
            LoggerFactory.getLogger(getClass()).error("Can't encrypt text", e);
        }
        return null;
    }

    @Override
    public String sign(String secret, String text)
    {
        return sign(secret + text);
    }
}
