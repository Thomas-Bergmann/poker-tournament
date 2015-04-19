package de.hatoka.address.internal.business;

import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBOLifecycleListener;
import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.exceptions.CountryNotExistException;
import de.hatoka.common.capi.exceptions.MandatoryParameterException;

public class AddressBOImpl implements AddressBO
{
    private static final CountryHelper COUNTRY_HELPER = new CountryHelper();
    private final AddressPO original;
    private final AddressBOLifecycleListener addressLifecycleListener;
    private final AddressDao addressDao;
    private AddressPO overridden = null;

    /**
     * @param addressPO
     *            (mandatory)
     * @param addressLifecycleListener
     *            (optional for non removable addresses)
     * @param uuidGenerator
     *            (optional for read only addresses)
     */
    public AddressBOImpl(AddressPO addressPO, AddressBOLifecycleListener addressLifecycleListener, AddressDao addressDao)
    {
        if (addressPO == null)
        {
            throw new MandatoryParameterException("addressPO");
        }
        this.addressLifecycleListener = addressLifecycleListener;
        this.addressDao = addressDao;
        original = addressPO;
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
        AddressBOImpl other = (AddressBOImpl)obj;
        if (original == null)
        {
            if (other.original != null)
                return false;
        }
        else if (!original.equals(other.original))
            return false;
        if (overridden == null)
        {
            if (other.overridden != null)
                return false;
        }
        else if (!overridden.equals(other.overridden))
            return false;
        return true;
    }

    @Override
    public String getCarryOver()
    {
        return getDelegateToRead().getCarryOver();
    }

    @Override
    public String getCity()
    {
        return getDelegateToRead().getCity();
    }

    @Override
    public String getCountryID()
    {
        return getDelegateToRead().getCountryID();
    }

    @Override
    public String getCounty()
    {
        return getDelegateToRead().getCounty();
    }

    private AddressPO getDelegateToRead()
    {
        if (overridden != null)
        {
            return overridden;
        }
        return original;
    }

    private synchronized AddressPO getDelegateToWrite()
    {
        if (overridden != null)
        {
            return overridden;
        }
        overridden = addressDao.clone(original);
        if (addressLifecycleListener != null)
        {
            addressLifecycleListener.cloned(this, original.getId());
        }
        return overridden;
    }

    @Override
    public String getID()
    {
        return getDelegateToRead().getId();
    }

    @Override
    public String getLine1()
    {
        return getDelegateToRead().getLine1();
    }

    @Override
    public String getLine2()
    {
        return getDelegateToRead().getLine2();
    }

    @Override
    public String getLine3()
    {
        return getDelegateToRead().getLine3();
    }


    @Override
    public String getName()
    {
        return getDelegateToRead().getName();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((original == null) ? 0 : original.hashCode());
        result = prime * result + ((overridden == null) ? 0 : overridden.hashCode());
        return result;
    }

    public boolean isModified()
    {
        if (overridden == null)
        {
            return false;
        }
        return !overridden.equals(original);
    }

    @Override
    public void remove()
    {
        if(addressLifecycleListener != null)
        {
            addressLifecycleListener.removedAddressRef(getID());
        }
    }

    @Override
    public void setCarryOver(String carryOver)
    {
        getDelegateToWrite().setCarryOver(carryOver);
    }

    @Override
    public void setCity(String city)
    {
        getDelegateToWrite().setCity(city);
    }

    @Override
    public void setCountryID(String countryID)
    {
        if (!COUNTRY_HELPER.existsCountry(countryID))
        {
            throw new CountryNotExistException(countryID);
        }
        getDelegateToWrite().setCountryID(countryID);

    }

    @Override
    public void setCounty(String county)
    {
        getDelegateToWrite().setCounty(county);
    }

    public void setId(String id)
    {
        getDelegateToWrite().setId(id);
    }

    @Override
    public void setLine1(String line1)
    {
        getDelegateToWrite().setLine1(line1);
    }

    @Override
    public void setLine2(String line2)
    {
        getDelegateToWrite().setLine2(line2);
    }

    @Override
    public void setLine3(String line3)
    {
        getDelegateToWrite().setLine3(line3);
    }

    @Override
    public void setName(String name)
    {
        getDelegateToWrite().setName(name);
    }

}
