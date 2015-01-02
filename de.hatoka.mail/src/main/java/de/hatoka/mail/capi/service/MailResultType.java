package de.hatoka.mail.capi.service;

public enum MailResultType
{
    NOT_SEND(1), INVALID(2), OK(4), ERROR_TEMPORARY(8), ERROR_PERMANENT(16), GENERAL(0x10000), RESULT_ABORTED(0x20000);

    private final int typeBit;

    private MailResultType(int typeBit)
    {
        this.typeBit = typeBit;
    }

    public int getTypeBit()
    {
        return typeBit;
    }
}
