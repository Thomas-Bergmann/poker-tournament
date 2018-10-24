package de.hatoka.user.internal.business;

import java.util.Locale;
import java.util.TimeZone;

import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.entities.UserPO;

public class UserBOImpl implements UserBO
{
    private static final CountryHelper COUNTRY_HELPER = new CountryHelper();

    private UserPO userPO;

    public UserBOImpl(UserPO userPO)
    {
        this.userPO = userPO;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserBOImpl other = (UserBOImpl)obj;
        if (userPO == null)
        {
            if (other.userPO != null)
                return false;
        }
        else if (!userPO.equals(other.userPO))
            return false;
        return true;
    }

    @Override
    public String getExternalRef()
    {
        return userPO.getExternalRef();
    }

    public Locale getLocale()
    {
        String locale = userPO.getLocale();
        if (locale == null)
        {
            return Locale.US;
        }
        return COUNTRY_HELPER.getLocale(locale);
    }

    public TimeZone getTimeZone()
    {
        String timeZone = userPO.getTimeZone();
        if (timeZone == null)
        {
            return CountryHelper.UTC;
        }
        return COUNTRY_HELPER.getTimeZone(timeZone);
    }

    public String getNickName()
    {
        return userPO.getNickName();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userPO == null) ? 0 : userPO.hashCode());
        return result;
    }

    @Override
    public boolean isActive()
    {
        return userPO.isActive();
    }

    @Override
    public void setLocale(Locale locale)
    {
        userPO.setLocale(locale.toString());
    }

    @Override
    public void setNickName(String nickName)
    {
        userPO.setNickName(nickName);
    }

    @Override
    public void setTimeZone(TimeZone timezone)
    {
        userPO.setTimeZone(timezone.getID());
    }
}
