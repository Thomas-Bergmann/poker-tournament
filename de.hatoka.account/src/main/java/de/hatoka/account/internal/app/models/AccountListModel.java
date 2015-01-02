package de.hatoka.account.internal.app.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AccountListModel
{
    private List<AccountVO> accounts = new ArrayList<>();

    public List<AccountVO> getAccounts()
    {
        return accounts;
    }

    public void setAccounts(List<AccountVO> accounts)
    {
        this.accounts = accounts;
    }
}
