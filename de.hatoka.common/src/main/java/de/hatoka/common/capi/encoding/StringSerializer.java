package de.hatoka.common.capi.encoding;

import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class StringSerializer<T extends Serializable>
{
    private ObjectSerializer<T> delegate = new ObjectSerializer<>();
    private final boolean isString;
    private Encoder encoder = Base64.getEncoder();
    private Decoder decoder = Base64.getDecoder();

    public StringSerializer(Class<T> toSerialize)
    {
        isString = toSerialize == String.class;
    }

    @SuppressWarnings("unchecked")
    public T deserialize(String serial) throws IOException
    {
        if (isString)
        {
            return (T)serial;
        }
        return delegate.convert(decoder.decode(serial.getBytes()));
    }

    public String serialize(T object) throws IOException
    {
        if (isString)
        {
            return (String)object;
        }
        return new String(encoder.encode(delegate.convert(object)));
    }
}
