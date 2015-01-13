package de.hatoka.tournament.internal.templates.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.hatoka.common.capi.app.model.MoneyVO;
import de.hatoka.common.capi.app.xslt.Processor;
import de.hatoka.common.capi.business.Money;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.LocalizationConstants;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.tournament.internal.app.models.CompetitorVO;
import de.hatoka.tournament.internal.app.models.PlayerVO;
import de.hatoka.tournament.internal.app.models.TournamentListModel;
import de.hatoka.tournament.internal.app.models.TournamentPlayerListModel;
import de.hatoka.tournament.internal.app.models.TournamentVO;

public class TournamentTemplateTest
{
    private static final String RESOURCE_PREFIX = "de/hatoka/tournament/internal/templates/app/";

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

    @BeforeClass
    public static void initClass()
    {
        XMLUnit.setIgnoreWhitespace(true);
    }

    private Processor getConverter(Locale locale)
    {
        return new Processor(RESOURCE_PREFIX, new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX
                        + "tournament", locale), "dd.MM.yyyy hh:mm"));
    }

    private PlayerVO getPlayerVO(String id, String name)
    {
        PlayerVO result = new PlayerVO();
        result.setId(id);
        result.setName(name);
        return result;
    }

    private String getResource(String string) throws IOException
    {
        StringWriter writer = new StringWriter();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PREFIX + string);
        if (resourceAsStream == null)
        {
            throw new FileNotFoundException("Can't find resource: " + RESOURCE_PREFIX + string);
        }
        IOUtils.copy(resourceAsStream, writer, "UTF-8");
        return writer.toString();
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
        StringWriter writer = new StringWriter();
        getConverter(Locale.US).process(model, "tournament_players.xslt", writer);
        XMLAssert.assertXMLEqual("players not listed correctly", getResource("tournament_players.result.xml"), writer.toString());
    }

    @Test
    @Ignore
    /**
     * TODO ignore - doesn't work at Travis CI
     * @throws Exception
     */
    public void testTournaments() throws Exception
    {
        TournamentListModel model = new TournamentListModel();
        model.getTournaments().add(
                        getTournamentVO("123456", "Test 1", new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT)
                                        .parse("2011-11-25T08:42:55.624+01:00")));
        model.getTournaments().add(
                        getTournamentVO("123457", "Test 2", new SimpleDateFormat(LocalizationConstants.XML_DATEFORMAT)
                                        .parse("2012-11-25T09:45:55.624+01:00")));
        StringWriter writer = new StringWriter();
        getConverter(Locale.US).process(model, "tournament_list.xslt", writer);
        XMLAssert.assertXMLEqual("tournaments not listed correctly", getResource("tournament_list.result.xml"), writer.toString());
    }
}
