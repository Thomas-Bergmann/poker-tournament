package de.hatoka.account.internal.app.models;

import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.account.capi.business.AccountBO;

@XmlRootElement
public class AccountVO
{
    private String id;
    private String name;
    private boolean isActive;
    private boolean isSelected;
    private String owner;

    public AccountVO()
    {
    }

    public AccountVO(AccountBO entity)
    {
        setId(entity.getID());
        setOwner(entity.getOwner().getEmail());
        setActive(entity.isActive());
        setName(entity.getName());
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getOwner()
    {
        return owner;
    }

    public boolean isActive()
    {
        return isActive;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }
}
