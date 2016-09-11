package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.hatoka.common.capi.app.model.SelectVO;

@XmlRootElement
public class TournamentConfigurationModel
{
    private TournamentVO tournament;
    private SelectVO reBuyOption = new SelectVO();
    private List<String> errors = new ArrayList<>();
    private String groupRef;

    public List<String> getErrors()
    {
        return errors;
    }

    public void setErrors(List<String> errors)
    {
        this.errors = errors;
    }

    public TournamentVO getTournament()
    {
        return tournament;
    }

    public void setTournament(TournamentVO tournament)
    {
        this.tournament = tournament;
    }

    public SelectVO getReBuyOption()
    {
        return reBuyOption;
    }

    public void setReBuyOption(SelectVO reBuyOption)
    {
        this.reBuyOption = reBuyOption;
    }

    public String getGroupRef()
    {
        return groupRef;
    }

    public void setGroupRef(String groupRef)
    {
        this.groupRef = groupRef;
    }
}
