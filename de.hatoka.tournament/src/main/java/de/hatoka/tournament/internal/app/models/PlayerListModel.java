package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PlayerListModel
{
    private List<PlayerVO> players = new ArrayList<>();

    public List<PlayerVO> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<PlayerVO> players)
    {
        this.players = players;
    }

}
