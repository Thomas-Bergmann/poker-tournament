package de.hatoka.account.internal.templates.app;

import java.io.IOException;
import java.util.Locale;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import de.hatoka.account.internal.app.models.AccountListModel;
import de.hatoka.account.internal.app.models.AccountVO;
import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.resource.ResourceLoader;

public class AccountTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/account/internal/templates/app/";
    private static final String XSLT_STYLESHEET = RESOURCE_PREFIX  + "account.xslt";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    private AccountVO getAccountVO(String id, String email, String name, boolean isActive)
    {
        AccountVO result = new AccountVO();
        result.setActive(isActive);
        result.setId(id);
        result.setOwner(email);
        result.setName(name);
        result.setSelected(isActive);
        return result;
    }

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void testListUsers() throws IOException, SAXException
    {
        AccountListModel model = new AccountListModel();
        model.getAccounts().add(getAccountVO("123456", "test1@test.mail", "Test 1", true));
        model.getAccounts().add(getAccountVO("123457", "test2@test.mail", "Test 2", false));
        String content = RENDERER.render(model, XSLT_STYLESHEET, RENDERER.getParameter(RESOURCE_PREFIX + "account", Locale.US));

        // Assert.assertEquals("account_list en_Us fails", getResource("account_list.result.xml"), content);
        XMLAssert.assertXMLEqual("account_list en_Us fails", RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + "account_list.result.xml"), content);
    }

}
