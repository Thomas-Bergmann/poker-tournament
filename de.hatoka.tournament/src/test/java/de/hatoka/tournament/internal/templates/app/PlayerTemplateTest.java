package de.hatoka.tournament.internal.templates.app;

import java.io.IOException;
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
import de.hatoka.tournament.internal.app.models.PlayerListModel;
import de.hatoka.tournament.internal.app.models.PlayerVO;

public class PlayerTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    private String getResource(String resource) throws IOException
    {
        return RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + resource);
    }

    private Map<String, Object> getParameter()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(Lib.XSLT_LOCALIZER, new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX + "tournament", Locale.US, CountryHelper.TZ_BERLIN)));
        return result;
    }

    private PlayerVO getPlayerVO(String id, String name, String eMail)
    {
        PlayerVO result = TournamentViewObjectHelper.getPlayerVO(id, name);
        result.seteMail(eMail);
        return result;
    }

    @Test
    public void testPlayerList() throws Exception
    {
        PlayerListModel model = new PlayerListModel();
        model.getPlayers().add(getPlayerVO("1234583", "Player 3", "mail3@test.hatoka.de"));
        model.getPlayers().add(getPlayerVO("1234582", "Player 2", "mail2@test.hatoka.de"));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "player_list.xslt", getParameter());
        // String content = new XMLRenderer().render(model);

        // Assert.assertEquals("players not listed correctly", getResource("player_list.result.xml"), content);
        XMLAssert.assertXMLEqual("players not listed correctly", getResource("player_list.result.xml"), content);
    }

}
