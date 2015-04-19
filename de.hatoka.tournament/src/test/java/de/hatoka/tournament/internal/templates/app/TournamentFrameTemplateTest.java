package de.hatoka.tournament.internal.templates.app;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.hatoka.common.capi.app.xslt.Lib;
import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.models.MenuItemVO;

public class TournamentFrameTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    private Map<String, Object> getParameter()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(Lib.XSLT_LOCALIZER, new ResourceLocalizer(
                        new LocalizationBundle(RESOURCE_PREFIX + "tournament", Locale.US, CountryHelper.UTC)));
        return result;
    }

    @Test
    @Ignore
    /**
     * TODO need to declare nbsp for XMLAssert
     * @throws Exception
     */
    public void testTournamentFrame() throws Exception
    {
        FrameModel model = new FrameModel();
        model.setTitle("Title Content");
        model.setContent("<p>Content</p>");
        model.setUriHome(URI.create("../home.xml"));

        model.addMainMenu("menu.list.cashgames", URI.create("../cash_games.xml"), false);
        MenuItemVO menu = model.addMainMenu("menu.cashgame.players", URI.create("players.xml"), true);
        menu.setTitle("Event 1");

        model.addSideMenu("menu.cashgame.players", URI.create("players.xml"), 3, URI.create("player_add.xml"), true);
        model.addSideMenu("menu.cashgame.history", URI.create("history.xml"), 4, null, false);

        String content = RENDERER.render(model, RESOURCE_PREFIX + "tournament_frame.xslt", getParameter());
        // String content = new XMLRenderer().render(model);

        // Assert.assertEquals("players not listed correctly", RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + "tournament_frame.result.xml"), content);
        XMLAssert.assertXMLEqual("players not listed correctly", RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + "tournament_frame.result.xml"), content);
    }

}
