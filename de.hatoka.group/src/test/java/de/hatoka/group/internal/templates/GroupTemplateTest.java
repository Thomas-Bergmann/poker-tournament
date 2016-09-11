package de.hatoka.group.internal.templates;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hatoka.common.capi.app.xslt.Lib;
import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.group.internal.app.list.GroupListItemVO;
import de.hatoka.group.internal.app.list.GroupListModel;

public class GroupTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/group/internal/templates/";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    private static String getResource(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + resource);
    }

    private static Map<String, Object> getParameter()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(Lib.XSLT_LOCALIZER, new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX + "group", Locale.US, CountryHelper.TZ_BERLIN)));
        return result;
    }

    private static GroupListItemVO getGroupListItemVO(String id, String name, int countMembers)
    {
        GroupListItemVO result = new GroupListItemVO(id, name, countMembers, true);
        result.setUri(URI.create("http://localhost/groups/" + result.getId() + "/members/list.html"));
        return result;
    }

    @Test
    public void testGroupList() throws Exception
    {
        GroupListModel model = new GroupListModel();
        model.getGroups().add(getGroupListItemVO("12345678", "Test Group 1", 1));
        model.getGroups().add(getGroupListItemVO("12345679", "Test Group 2", 2));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "group_list.xslt", getParameter());
        // String content = new XMLRenderer().render(model);

        assertEquals("groups not listed correctly", getResource("group_list.result.xml"), content);
        XMLAssert.assertXMLEqual("groups not listed correctly", getResource("group_list.result.xml"), content);
    }

}
