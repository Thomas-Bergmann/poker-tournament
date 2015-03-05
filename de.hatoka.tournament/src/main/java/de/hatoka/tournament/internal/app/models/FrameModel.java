package de.hatoka.tournament.internal.app.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class FrameModel
{
    @XmlElementWrapper(name = "mainMenu")
    @XmlElement(name = "menuItem")
    private List<MenuItemVO> mainMenuItems = new ArrayList<>();

    @XmlElementWrapper(name = "sideMenu")
    @XmlElement(name = "menuItem")
    private List<MenuItemVO> sideNavItems = new ArrayList<>();

    @XmlAttribute
    private String title = null;

    @XmlElement
    private String content = null;

    @XmlAttribute
    private URI uriHome = null;

    public FrameModel()
    {
    }

    public MenuItemVO addMainMenu(String title, URI uri, boolean isActive)
    {
        MenuItemVO menuItemVO = new MenuItemVO(title, uri, isActive);
        getMainMenuItems().add(menuItemVO);
        return menuItemVO;
    }

    public MenuItemVO addSiteMenu(String title, URI uriList, Integer count, URI uriAdd, boolean isActive)
    {
        MenuItemVO menuItemVO = new MenuItemVO(title, uriList, isActive, count, uriAdd);
        getSideNavItems().add(menuItemVO);
        return menuItemVO;
    }

    @XmlTransient
    public List<MenuItemVO> getMainMenuItems()
    {
        return mainMenuItems;
    }

    @XmlTransient
    public List<MenuItemVO> getSideNavItems()
    {
        return sideNavItems;
    }

    @XmlTransient
    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    @XmlTransient
    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setMainMenuItems(List<MenuItemVO> mainMenuItems)
    {
        this.mainMenuItems = mainMenuItems;
    }

    public void setSideNavItems(List<MenuItemVO> sideNavItems)
    {
        this.sideNavItems = sideNavItems;
    }

    @XmlTransient
    public URI getUriHome()
    {
        return uriHome;
    }

    public void setUriHome(URI uriHome)
    {
        this.uriHome = uriHome;
    }
}
