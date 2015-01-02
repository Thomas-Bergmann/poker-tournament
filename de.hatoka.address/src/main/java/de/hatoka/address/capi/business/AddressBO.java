package de.hatoka.address.capi.business;

import de.hatoka.common.capi.business.BusinessObject;


public interface AddressBO extends BusinessObject
{
    public String getCarryOver();
    public String getCity();
    public String getCountryID();
    public String getCounty();
    public String getLine1();
    public String getLine2();
    public String getLine3();
    public String getName();

    public void setCarryOver(String carryOver);
    public void setCity(String city);
    public void setCountryID(String countryID);
    public void setCounty(String county);
    public void setLine1(String line1);
    public void setLine2(String line2);
    public void setLine3(String line3);
    public void setName(String name);
}
