package de.hatoka.jndi.account;

import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class ClientConfigurationBeanFactory implements ObjectFactory
{

    public Object getObjectInstance(Object obj, Name jndiName, Context nameCtx,
                    @SuppressWarnings("rawtypes") Hashtable environment) throws NamingException
    {

        // Acquire an instance of our specified bean class
        ClientConfigurationBean bean = new ClientConfigurationBean();

        // Customize the bean properties from our attributes
        Reference ref = (Reference)obj;
        Enumeration<RefAddr> addrs = ref.getAll();
        while(addrs.hasMoreElements())
        {
            RefAddr addr = addrs.nextElement();
            String name = addr.getType();
            String value = (String)addr.getContent();
            if (name.equals("loginURI"))
            {
                bean.setLoginURI(URI.create(value));
            }
        }

        // Return the customized instance
        return (bean);

    }

}