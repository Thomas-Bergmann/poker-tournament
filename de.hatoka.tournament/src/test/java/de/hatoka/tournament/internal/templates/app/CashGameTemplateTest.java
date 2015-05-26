package de.hatoka.tournament.internal.templates.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import de.hatoka.common.capi.business.CountryHelper;
import de.hatoka.common.capi.business.Money;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.LocalizationConstants;
import de.hatoka.common.capi.resource.ResourceLoader;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.tournament.capi.types.HistoryEntryType;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.HistoryEntryVO;
import de.hatoka.tournament.internal.app.models.HistoryModel;
import de.hatoka.tournament.internal.app.models.PlayerVO;
import de.hatoka.tournament.internal.app.models.TournamentPlayerListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

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
                        new LocalizationBundle(RESOURCE_PREFIX + "tournament", Locale.US, CountryHelper.UTC)));
        return result;
    }

    private TournamentVO getTournamentVO(String id, String name, Date date)
    {
        return TournamentViewObjectHelper.getTournamentVO(id, name, date);
    }
    private CompetitorVO getCompetitorVO(String id, String name, String playerID)
    {
        return TournamentViewObjectHelper.getCompetitorVO(id, name, playerID);
    }
    private PlayerVO getPlayerVO(String id, String name)
    {
        return TournamentViewObjectHelper.getPlayerVO(id, name);
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
                        LocalizationConstants.XML_DATEFORMAT_MILLIS).parse("2012-11-26T00:45:55.624+01:00")));
        model.getEntries().add(getHistoryEntry("Player 2",HistoryEntryType.ReBuy, new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT_MILLIS).parse("2012-11-26T01:45:55.624+00:00")));
        model.getEntries().add(getHistoryEntry("Player 3", HistoryEntryType.CashOut, new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT_MILLIS).parse("2012-11-26T02:45:55.624+01:00")));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "cashgame_history.xslt", getParameter());
        // Assert.assertEquals("history not listed correctly", getResource("cashgame_history.result.xml"), content);
        XMLAssert.assertXMLEqual("history not listed correctly", getResource("cashgame_history.result.xml"), content);
    }

    @Test
    public void testGashgamePlayers() throws Exception
    {
        TournamentPlayerListModel model = new TournamentPlayerListModel();
        model.setTournament(getTournamentVO("123456", "Test 1", new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT_MILLIS).parse("2011-11-25T08:42:55.624+01:00")));
        model.getCompetitors().add(getCompetitorVO("1234578", "Player 1", "playerid-1"));
        CompetitorVO competitorVO = getCompetitorVO("1234579", "Player 2", "playerid-2");
        competitorVO.setResult(new MoneyVO(Money.valueOf("-1", "USD")));
        competitorVO.setActive(false);
        model.getCompetitors().add(competitorVO);
        model.getUnassignedPlayers().add(getPlayerVO("1234581", "Player 3"));
        model.getErrors().addAll(Arrays.asList("error.number.format","error.select.player"));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "cashgame_players.xslt", getParameter());

        // Assert.assertEquals("players not listed correctly", getResource("cashgame_players.result.xml"), content);
        XMLAssert.assertXMLEqual("players not listed correctly", getResource("cashgame_players.result.xml"), content);
    }

}
