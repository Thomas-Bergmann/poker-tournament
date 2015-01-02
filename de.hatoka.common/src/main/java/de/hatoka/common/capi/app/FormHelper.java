package de.hatoka.common.capi.app;

public class FormHelper
{
    public boolean isEmpty(String... values)
    {
        for (String value : values)
        {
            if (value == null || value.trim().isEmpty())
            {
                return true;
            }
        }
        return false;
    }

}
