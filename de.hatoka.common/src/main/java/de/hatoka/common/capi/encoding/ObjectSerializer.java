package de.hatoka.common.capi.encoding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ObjectSerializer<T extends Serializable>
{
    public T convert(byte[] yourBytes) throws IOException
    {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes))
        {
            try (ObjectInput in = new ObjectInputStream(bis))
            {
                @SuppressWarnings("unchecked")
                T object = (T)in.readObject();
                return object;
            }
            catch(ClassNotFoundException e)
            {
                throw new IOException("Class for serialized object not found", e);
            }
        }
    }

    public byte[] convert(T object) throws IOException
    {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream())
        {
            try (ObjectOutput out = new ObjectOutputStream(bos))
            {
                out.writeObject(object);
                return bos.toByteArray();
            }
        }
    }
}
