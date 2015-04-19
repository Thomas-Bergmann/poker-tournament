package de.hatoka.tournament.internal.templates.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
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
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.PlayerVO;
import de.hatoka.tournament.internal.app.models.TournamentListModel;
import de.hatoka.tournament.internal.app.models.TournamentPlayerListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TournamentTemplateTest
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

    private String wrapXMLRoot(String content) throws IOException
    {
        return "<body>\n" + content + "\n</body>";
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

    @Test
    public void testTournamentPlayers() throws Exception
    {
        TournamentPlayerListModel model = new TournamentPlayerListModel();
        model.setTournament(getTournamentVO("123456", "Test 1", new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT).parse("2011-11-25T08:42:55.624+01:00")));
        model.getCompetitors().add(getCompetitorVO("1234578", "Player 1", "playerid-1"));
        CompetitorVO competitorVO = getCompetitorVO("1234579", "Player 2", "playerid-2");
        competitorVO.setResult(new MoneyVO(Money.getInstance("-1", "USD")));
        competitorVO.setActive(false);
        model.getCompetitors().add(competitorVO);
        model.getUnassignedPlayers().add(getPlayerVO("1234581", "Player 3"));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "tournament_players.xslt", getParameter());

        // Assert.assertEquals("players not listed correctly", getResource("tournament_players.result.xml"), content);
        XMLAssert.assertXMLEqual("players not listed correctly", getResource("tournament_players.result.xml"), content);
    }

    @Test
    public void testTournamentNoUnassignedPlayers() throws Exception
    {
        TournamentPlayerListModel model = new TournamentPlayerListModel();
        model.setTournament(getTournamentVO("123456", "Test 1", new SimpleDateFormat(
                        LocalizationConstants.XML_DATEFORMAT).parse("2011-11-25T08:42:55.624+01:00")));
        model.getCompetitors().add(getCompetitorVO("1234578", "Player 1", "playerid-1"));
        CompetitorVO competitorVO = getCompetitorVO("1234579", "Player 2", "playerid-2");
        competitorVO.setResult(new MoneyVO(Money.getInstance("-1", "USD")));
        competitorVO.setActive(false);
        model.getCompetitors().add(competitorVO);
        String content = RENDERER.render(model, RESOURCE_PREFIX + "tournament_player_add.xslt", getParameter());

        // Assert.assertEquals("players not listed correctly", getResource("tournament_no_unassigned.result.xml"), content);
        XMLAssert.assertXMLEqual("players unassigned incorrectly", wrapXMLRoot(getResource("tournament_no_unassigned.result.xml")), wrapXMLRoot(content));
    }

    @Test
    public void testTournaments() throws Exception
    {
        TournamentListModel model = new TournamentListModel();
        model.getTournaments().add(
                        getTournamentVO("123456", "Test 1", new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT)
                        .parse("2011-11-25T07:42:55.624+01:00")));
        model.getTournaments().add(
                        getTournamentVO("123457", "Test 2", new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS)
                        .parse("2012-11-25T08:45:55+01:00")));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "tournament_list.xslt", getParameter());
        Assert.assertEquals("tournaments not listed correctly", getResource("tournament_list.result.xml"), content);
        XMLAssert.assertXMLEqual("tournaments not listed correctly", getResource("tournament_list.result.xml"), content);
    }
}
