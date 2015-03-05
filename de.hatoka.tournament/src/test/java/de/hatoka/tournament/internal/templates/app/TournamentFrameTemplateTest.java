package de.hatoka.tournament.internal.templates.app;

import java.io.StringWriter;
import java.net.URI;
import java.util.Locale;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hatoka.common.capi.app.xslt.Processor;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.models.MenuItemVO;

public class TournamentFrameTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";
    private static final ResourceLoader RESOURCE_LOADER = new ResourceLoader();
    private static final Processor PROCESSOR = new Processor(RESOURCE_PREFIX, new ResourceLocalizer(
                    new LocalizationBundle(RESOURCE_PREFIX + "tournament", Locale.US), "dd.MM.yyyy hh:mm"));

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    @Test
    public void testTournamentFrame() throws Exception
    {
        FrameModel model = new FrameModel();
        model.setTitle("Title Content");
        model.setContent("<p>Content</p>");
        model.setUriHome(URI.create("../home.xml"));

        model.addMainMenu("menu.list.cashgames", URI.create("../cash_games.xml"), false);
        MenuItemVO menu = model.addMainMenu("menu.cashgame.players", URI.create("players.xml"), true);
        menu.setTitle("Event 1");

        model.addSiteMenu("menu.cashgame.players", URI.create("players.xml"), 3, URI.create("player_add.xml"), true);
        model.addSiteMenu("menu.cashgame.history", URI.create("history.xml"), 4, null, false);

        StringWriter writer = new StringWriter();
        // getConverter(Locale.US).process(model, writer);
        PROCESSOR.process(model, "tournament_frame.xslt", writer);

        // Assert.assertEquals("players not listed correctly", resourceLoader.getResourceAsString(RESOURCE_PREFIX + "tournament_frame.result.xml"), writer.toString());
        XMLAssert.assertXMLEqual("players not listed correctly", RESOURCE_LOADER.getResourceAsString(RESOURCE_PREFIX + "tournament_frame.result.xml"), writer.toString());
    }

}
