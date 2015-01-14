package de.hatoka.account.internal.business;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang.LocaleUtils;
import org.slf4j.LoggerFactory;

import de.hatoka.account.capi.business.AccountBORepository;
import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.business.UserBO;
import de.hatoka.account.capi.config.AccountMailConfiguration;
import de.hatoka.account.capi.dao.UserDao;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.account.internal.app.models.SignUpVerifyMailModel;
import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBORepository;
import de.hatoka.common.capi.app.xslt.Processor;
import de.hatoka.common.capi.dao.EncryptionUtils;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.capi.exceptions.IllegalObjectAccessException;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.mail.capi.dao.MailDao;
import de.hatoka.mail.capi.entities.MailPO;
import de.hatoka.mail.capi.service.MailAddressType;
import de.hatoka.mail.capi.service.MailService;

public class UserBOImpl implements UserBO
{
    private static final String RESOURCE_PREFIX = "de/hatoka/account/internal/templates/mail/";

    @Inject
    private AccountBusinessFactory businessFactory;
    @Inject
    private UserDao userDao;
    @Inject
    private MailDao mailDao;
    @Inject
    private MailService mailService;
    @Inject
    private UUIDGenerator uuidGenerator;
    @Inject
    private AccountMailConfiguration mailConfiguration;
    @Inject
    private EncryptionUtils encryptionUtils;

    private UserPO userPO;

    public UserBOImpl(UserPO userPO)
    {
        this.userPO = userPO;
    }

    @Override
    public boolean applySignInToken(String token)
    {
        if (userPO.getSignInToken() != null)
        {
            if (userPO.getSignInToken().equals(token))
            {
                userPO.setActive(true);
                userPO.setSignInToken(null);
                userPO.setEmailIsVerified(true);
                return true;
            }
            return false;
        }
        return userPO.isActive();
    }

    private MailPO createSignUpVerifyMail(URI uri, String accountID)
    {
        // create content
        Processor processor = getProcessor();
        StringWriter writer = new StringWriter();
        SignUpVerifyMailModel signUpVerifyEmailModel = new SignUpVerifyMailModel();
        String link = uri.toString() + "?token=" + userPO.getSignInToken() + "&email=" + userPO.getEmail();
        signUpVerifyEmailModel.setLink(link);
        try
        {
            processor.process(signUpVerifyEmailModel, "signUpVerifyEmail.html.xslt", writer);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Can't create verification email content.", e);
        }
        // create mail
        MailPO result = mailDao.createAndInsert(accountID);
        result.setSubject(getLocalizer().getText("signup.mail.subject", "Email Verification"));
        result.setContentHTML(writer.toString());
        mailDao.createReceiver(result, MailAddressType.FROM, mailConfiguration.getFromAddressForAccountRegistration());
        mailDao.createReceiver(result, MailAddressType.TO, userPO.getEmail());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserBOImpl other = (UserBOImpl)obj;
        if (userPO == null)
        {
            if (other.userPO != null)
                return false;
        }
        else if (!userPO.equals(other.userPO))
            return false;
        return true;
    }

    @Override
    public AccountBORepository getAccountBORepository()
    {
        return businessFactory.getAccountBORepository(userPO);
    }

    @Override
    public AddressBORepository getAddressBORepository()
    {
        return businessFactory.getAddressBORepository(userPO);
    }

    @Override
    public AddressBO getBusinessAddressBO()
    {
        String addressRef = userPO.getBusinessAddressRef();
        if (addressRef == null)
        {
            return null;
        }
        return getAddressBORepository().getByID(addressRef);
    }

    @Override
    public String getEmail()
    {
        return userPO.getEmail();
    }

    @Override
    public String getID()
    {
        return userPO.getId();
    }

    private Locale getLocale()
    {
        String locale = userPO.getLocale();
        if (locale == null)
        {
            return Locale.US;
        }
        return LocaleUtils.toLocale(locale);
    }

    private ResourceLocalizer getLocalizer()
    {
        return new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX + "signup", getLocale()));
    }

    @Override
    public String getNickName()
    {
        return userPO.getNickName();
    }

    @Override
    public AddressBO getPrivateAddressBO()
    {
        String addressRef = userPO.getPrivateAddressRef();
        if (addressRef == null)
        {
            return null;
        }
        return getAddressBORepository().getByID(addressRef);
    }

    private Processor getProcessor()
    {
        return new Processor(RESOURCE_PREFIX, getLocalizer());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userPO == null) ? 0 : userPO.hashCode());
        return result;
    }

    @Override
    public boolean isActive()
    {
        return userPO.isActive();
    }

    @Override
    public void register(String email, String password)
    {
        if (userPO.getEmail() != null)
        {
            throw new IllegalStateException("User has email, registration not possible");
        }
        userPO.setEmail(email);
        userPO.setPassword(encryptionUtils.sign(password));
        userPO.setActive(false);
        userPO.setEmailIsVerified(false);
    }

    @Override
    public void remove()
    {
        userDao.remove(userPO);
        userPO = null;
    }

    @Override
    public void sendEmailVerificationMail(URI uri)
    {
        if (userPO.getEmail() == null)
        {
            throw new RuntimeException("User has no email address.");
        }
        userPO.setSignInToken(uuidGenerator.generate());
        MailPO mail = createSignUpVerifyMail(uri, userPO.getAccountPOs().iterator().next().getId());
        mailService.sendMail(mail);
    }

    @Override
    public void setBusinessAddressBO(AddressBO addressBO)
    {
        if (getAddressBORepository().contains(addressBO))
        {
            userPO.setBusinessAddressRef(addressBO.getID());
        }
        else
        {
            throw new IllegalObjectAccessException("Address is not owned by user.");
        }
    }

    @Override
    public void setLocale(Locale locale)
    {
        userPO.setLocale(locale.toString());
    }

    @Override
    public void setNickName(String nickName)
    {
        userPO.setNickName(nickName);
    }

    @Override
    public void setPrivateAddressBO(AddressBO addressBO)
    {
        if (getAddressBORepository().contains(addressBO))
        {
            userPO.setPrivateAddressRef(addressBO.getID());
        }
        else
        {
            throw new IllegalObjectAccessException("Address is not owned by user.");
        }
    }

    @Override
    public boolean verifyPassword(String password)
    {
        if(!userPO.isActive())
        {
            return false;
        }
        String stored = userPO.getPassword();
        if (stored == null)
        {
            LoggerFactory.getLogger(getClass()).warn("User with id '{}' had no password.", userPO.getId());
            userPO.setPassword("no-password");
            return false;
        }
        return stored.equals(encryptionUtils.sign(password));
    }
}
