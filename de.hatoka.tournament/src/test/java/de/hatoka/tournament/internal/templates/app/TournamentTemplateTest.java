package de.hatoka.tournament.internal.templates.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.common.capi.app.xslt.Lib;
import de.hatoka.common.capi.app.xslt.XSLTRenderer;
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

    private CompetitorVO getCompetitorVO(String id, String name, String playerID)
    {
        CompetitorVO result = new CompetitorVO();
        result.setId(id);
        result.setPlayerName(name);
        result.setPlayerId(playerID);
        result.setInPlay(new MoneyVO(Money.ONE_USD));
        result.setResult(new MoneyVO(Money.NOTHING));
        result.setActive(true);
        return result;
    }

    private Map<String, Object> getParameter()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(Lib.XSLT_LOCALIZER, new ResourceLocalizer(
                        new LocalizationBundle(RESOURCE_PREFIX + "tournament", Locale.US), "dd.MM.yyyy hh:mm"));
        return result;
    }

    private PlayerVO getPlayerVO(String id, String name)
    {
        PlayerVO result = new PlayerVO();
        result.setId(id);
        result.setName(name);
        return result;
    }

    private TournamentVO getTournamentVO(String id, String name, Date date)
    {
        TournamentVO result = new TournamentVO();
        result.setId(id);
        result.setName(name);
        result.setDate(date);
        result.setBuyIn(new MoneyVO(Money.ONE_USD));
        result.setUri(UriBuilder.fromPath("tournament/{id}/players.html").build(id));
        result.setAverage(new MoneyVO(Money.ONE_USD));
        result.setSumInPlay(new MoneyVO(Money.getInstance("2", "USD")));
        result.setCompetitorsSize(10);
        return result;
    }

    @Test
    public void testTournamentPlayers() throws Exception
    {
        TournamentPlayerListModel model = new TournamentPlayerListModel(UriBuilder.fromPath("list.html").build());
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
        TournamentPlayerListModel model = new TournamentPlayerListModel(UriBuilder.fromPath("list.html").build());
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
    @Ignore
    /**
     * FIXME #8 ignore - doesn't work at Travis CI
     * @throws Exception
     */
    public void testTournaments() throws Exception
    {
        TournamentListModel model = new TournamentListModel();
        model.getTournaments().add(
                        getTournamentVO("123456", "Test 1", new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT)
                                        .parse("2011-11-25T08:42:55.624+01:00")));
        model.getTournaments().add(
                        getTournamentVO("123457", "Test 2", new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT_SECONDS)
                                        .parse("2012-11-25T09:45:55+01:00")));
        String content = RENDERER.render(model, RESOURCE_PREFIX + "tournament_list.xslt", getParameter());
        // Assert.assertEquals("tournaments not listed correctly", getResource("tournament_list.result.xml"), content);
        XMLAssert.assertXMLEqual("tournaments not listed correctly", getResource("tournament_list.result.xml"), content);
    }
}
