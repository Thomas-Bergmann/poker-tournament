package de.hatoka.tournament.internal.templates.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.common.capi.app.xslt.Lib;
import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.business.Money;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.LocalizationConstants;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.tournament.capi.entities.HistoryEntryType;
import de.hatoka.tournament.internal.app.models.HistoryEntryVO;
import de.hatoka.tournament.internal.app.models.HistoryModel;

public class CashGameTemplateTest
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
        result.put(Lib.XSLT_LOCALIZER, new ResourceLocalizer(
                        new LocalizationBundle(RESOURCE_PREFIX + "tournament", Locale.US)));
        return result;
    }

    private HistoryEntryVO getHistoryEntry(String player, HistoryEntryType entry, Date date)
    {
        HistoryEntryVO result = new HistoryEntryVO();
        result.setPlayerName(player);
        result.setDate(date);
        result.setAmount(new MoneyVO(Money.ONE_USD));
        result.setEntry(entry);
        return result;
    }

    @Test
    public void testCashgameHistory() throws Exception
    {
        HistoryModel model = new HistoryModel();
        model.getEntries().add(getHistoryEntry("Player 1", HistoryEntryType.BuyIn, new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT).parse("2012-11-26T00:45:55.624+01:00")));
        model.getEntries().add(getHistoryEntry("Player 2",HistoryEntryType.ReBuy, new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT).parse("2012-11-26T01:45:55.624+01:00")));
        model.getEntries().add(getHistoryEntry("Player 3", HistoryEntryType.CashOut, new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT).parse("2012-11-26T02:45:55.624+01:00")));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "cashgame_history.xslt", getParameter());
        // Assert.assertEquals("history not listed correctly", getResource("cashgame_history.result.xml"), content);
        XMLAssert.assertXMLEqual("history not listed correctly", getResource("cashgame_history.result.xml"), content);
    }

}
