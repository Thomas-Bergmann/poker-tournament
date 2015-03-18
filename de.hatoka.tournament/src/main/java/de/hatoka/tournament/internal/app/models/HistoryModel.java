package de.hatoka.tournament.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HistoryModel
{
    private List<HistoryEntryVO> entries = new ArrayList<>();

    public List<HistoryEntryVO> getEntries()
    {
        return entries;
    }

    public void setEntries(List<HistoryEntryVO> entries)
    {
        this.entries = entries;
    }
}
