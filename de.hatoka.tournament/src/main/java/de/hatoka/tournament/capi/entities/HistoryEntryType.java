package de.hatoka.tournament.capi.entities;

public enum HistoryEntryType
{
    BuyIn, ReBuy, CashOut;

    public String getTemplateKey()
    {
        return "history.action." + name();
    }
}
