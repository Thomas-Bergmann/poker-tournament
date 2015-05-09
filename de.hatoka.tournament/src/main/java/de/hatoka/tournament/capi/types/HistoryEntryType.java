package de.hatoka.tournament.capi.types;

public enum HistoryEntryType
{
    BuyIn, ReBuy, CashOut;

    public String getTemplateKey()
    {
        return "history.action." + name();
    }
}
