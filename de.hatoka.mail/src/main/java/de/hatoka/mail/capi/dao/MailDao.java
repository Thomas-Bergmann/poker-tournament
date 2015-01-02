package de.hatoka.mail.capi.dao;

import java.io.File;
import java.io.IOException;

import de.hatoka.common.capi.dao.Dao;
import de.hatoka.mail.capi.entities.MailAttachmentPO;
import de.hatoka.mail.capi.entities.MailPO;
import de.hatoka.mail.capi.entities.MailReceiverPO;
import de.hatoka.mail.capi.entities.MailResultReceiverPO;
import de.hatoka.mail.capi.service.MailAddressType;
import de.hatoka.mail.capi.service.MailResultType;

public interface MailDao extends Dao<MailPO>
{
    MailPO createAndInsert(String accountRef);

    MailAttachmentPO createAttachment(MailPO mail, File file) throws IOException;

    MailReceiverPO createReceiver(MailPO mail, MailAddressType type, String address);

    public MailResultReceiverPO createResult(MailReceiverPO receiver, MailResultType type);
}
