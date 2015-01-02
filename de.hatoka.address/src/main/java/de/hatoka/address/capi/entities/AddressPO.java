package de.hatoka.address.capi.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import de.hatoka.common.capi.exceptions.CopyNotSupportException;

@Entity
public class AddressPO implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @NotNull
    private String name;
    private String carryOver;
    @NotNull
    private String line1;
    private String line2;
    private String line3;
    @NotNull
    private String city;
    private String county;
    @NotNull
    private String countryID;
    private String predecessorRef;

    public AddressPO()
    {
    }

    public AddressPO(String id)
    {
        setId(id);
    }

    @Override
    public AddressPO clone()
    {
        try
        {
            return (AddressPO)super.clone();
        }
        catch(CloneNotSupportedException e)
        {
            throw new CopyNotSupportException(e);
        }
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
        AddressPO other = (AddressPO)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    public String getCarryOver()
    {
        return carryOver;
    }

    public String getCity()
    {
        return city;
    }

    public String getCountryID()
    {
        return countryID;
    }

    public String getCounty()
    {
        return county;
    }

    public String getId()
    {
        return id;
    }

    public String getLine1()
    {
        return line1;
    }

    public String getLine2()
    {
        return line2;
    }

    public String getLine3()
    {
        return line3;
    }

    public String getName()
    {
        return name;
    }

    public String getPredecessorRef()
    {
        return predecessorRef;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public void setCarryOver(String carryOver)
    {
        this.carryOver = carryOver;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setCountryID(String countryID)
    {
        this.countryID = countryID;
    }

    public void setCounty(String county)
    {
        this.county = county;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setLine1(String line1)
    {
        this.line1 = line1;
    }

    public void setLine2(String line2)
    {
        this.line2 = line2;
    }

    public void setLine3(String line3)
    {
        this.line3 = line3;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPredecessorRef(String predecessorRef)
    {
        this.predecessorRef = predecessorRef;
    }

}
