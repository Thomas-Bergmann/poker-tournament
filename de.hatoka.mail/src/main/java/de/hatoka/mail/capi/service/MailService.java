package de.hatoka.mail.capi.service;

import de.hatoka.mail.capi.entities.MailPO;

public interface MailService
{
    public MailResultType sendMail(MailPO mailPO);
}
