package de.hatoka.common.capi.business;

public class Warning
{
    public static Warning valueOf(String messageKey, Object... parameter)
    {
        return new Warning(messageKey, parameter);
    }

    private final String messageKey;
    private final Object[] parameters;

    public Warning(String messageKey, Object... parameter)
    {
        this.messageKey = messageKey;
        this.parameters = parameter;
    }

    public String getMessageKey()
    {
        return messageKey;
    }

    public Object[] getParameters()
    {
        return parameters;
    }
}
