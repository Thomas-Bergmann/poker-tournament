package de.hatoka.mail.capi.service;

public enum MailAddressType
{
    TO(0), CC(1), BCC(2), FROM(3), REPLY(4);

    public static final MailAddressType getBy(int type)
    {
        switch (type)
        {
        case 0 :
            return TO;
        case 1 :
            return CC;
        case 2 :
            return BCC;
        case 3 :
            return FROM;
        case 4 :
            return REPLY;
        }
        return null;
    }

    private final int addressType;

    private MailAddressType(int addressType)
    {
        this.addressType = addressType;
    }

    public int getAddressType()
    {
        return addressType;
    }
}
