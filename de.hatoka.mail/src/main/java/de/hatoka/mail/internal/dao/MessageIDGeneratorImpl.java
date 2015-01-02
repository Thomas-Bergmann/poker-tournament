package de.hatoka.mail.internal.dao;

import javax.inject.Inject;

import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.mail.capi.dao.MessageIDGenerator;

public class MessageIDGeneratorImpl implements MessageIDGenerator
{
    @Inject
    UUIDGenerator uuidGenerator;

    @Override
    public String generate(String accountRef)
    {
        StringBuilder messageID = new StringBuilder();

        // prefix letters to uuid to be sure, it is a valid email syntax
        messageID.append('<');
        messageID.append('I');

        // create unique id
        messageID.append(uuidGenerator.generate());

        messageID.append('D');
        messageID.append('@');

        messageID.append(accountRef);

        messageID.append('>');

        return messageID.toString();
    }

}
