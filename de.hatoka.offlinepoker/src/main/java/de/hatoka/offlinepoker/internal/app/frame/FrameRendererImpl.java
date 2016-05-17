package de.hatoka.offlinepoker.internal.app.frame;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.UriInfo;

import de.hatoka.common.capi.app.FrameRenderer;
import de.hatoka.common.capi.app.servlet.RequestUserRefProvider;
import de.hatoka.common.capi.app.xslt.XSLTRenderer;
import de.hatoka.common.capi.resource.LocalizationBundle;
import de.hatoka.common.capi.resource.ResourceLocalizer;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupBusinessFactory;
import de.hatoka.group.internal.app.servlets.GroupListService;
import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.capi.business.TournamentBusinessFactory;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.servlets.CashGameCompetitorService;
import de.hatoka.tournament.internal.app.servlets.CashGameListService;
import de.hatoka.tournament.internal.app.servlets.PlayerListService;
import de.hatoka.tournament.internal.app.servlets.TournamentBlindLevelService;
import de.hatoka.tournament.internal.app.servlets.TournamentCompetitorService;
import de.hatoka.tournament.internal.app.servlets.TournamentListService;
import de.hatoka.tournament.internal.app.servlets.TournamentRankService;
import de.hatoka.tournament.internal.app.servlets.TournamentService;
import de.hatoka.tournament.internal.app.servlets.TournamentTableService;

public class FrameRendererImpl implements FrameRenderer
{
    @Inject
    private TournamentBusinessFactory tournamentFactory;
    @Inject
    private GroupBusinessFactory groupFactory;
    @Inject
    private RequestUserRefProvider userRefProvider;
    @Inject
    private Provider<UriInfo> uriInfoProvider;
    @Inject
    private Provider<Locale> localeProvider;
    @Inject
    private Provider<TimeZone> timeZoneProvider;

    private static final String RESOURCE_PREFIX = "de/hatoka/offlinepoker/internal/templates/app/";
    private static final XSLTRenderer RENDERER = new XSLTRenderer();

    @Override
    public String renderFame(String content, String... selectedItems)
    {
        TournamentBORepository tournamentBORepository = tournamentFactory .getTournamentBORepository(userRefProvider.getUserRef());
        FrameModel frameModel = null;
        String mainItem = selectedItems[0];
        String level1Item = selectedItems[1];
        if ("cashgame".equals(mainItem))
        {
            frameModel = getCashGameFrameModel(content, getSingleTitle(selectedItems), uriInfoProvider.get(), tournamentBORepository,
                            level1Item);
        }
        else if ("tournament".equals(mainItem))
        {
            frameModel = getTournamentFrameModel(content, getSingleTitle(selectedItems), uriInfoProvider.get(), tournamentBORepository,
                            level1Item);
        }
        else
        {
            PlayerBORepository playerBORepository = tournamentFactory.getPlayerBORepository(userRefProvider.getUserRef());
            GroupBORepository groupBORepository = groupFactory.getGroupBORepository(userRefProvider.getUserRef());
            frameModel = getMainFrameModel(content, getTitle(selectedItems), uriInfoProvider.get(), tournamentBORepository,
                            playerBORepository, groupBORepository, mainItem);
        }
        try
        {
            return RENDERER.render(frameModel, RESOURCE_PREFIX + "frame.xslt", getXsltProcessorParameter("frame"));
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static String getTitle(String[] selectedItems)
    {
        StringBuilder builder = new StringBuilder("title");
        for(String item : selectedItems)
        {
            builder.append(".").append(item);
        }
        return builder.toString();
    }

    private static String getSingleTitle(String[] selectedItems)
    {
        StringBuilder builder = new StringBuilder("title");
        builder.append(".").append(selectedItems[0]);
        builder.append(".").append(selectedItems[2]);
        return builder.toString();
    }

    /**
     * @param localizationResource
     *            without resourcePrefix
     * @return parameter for xslt processor
     */
    private Map<String, Object> getXsltProcessorParameter(String localizationResource)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("uriInfo", uriInfoProvider.get());
        if (localizationResource != null)
        {
            result.put("localizer", new ResourceLocalizer(new LocalizationBundle(RESOURCE_PREFIX + localizationResource,
                            localeProvider.get(), timeZoneProvider.get())));
        }
        return result;
    }

    private static URI getUri(UriInfo info, Class<?> resource, String methodName, Object... values)
    {
        return info.getBaseUriBuilder().path(resource).path(resource, methodName).build(values);
    }

    private static FrameModel getMainFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, PlayerBORepository playerBORepository,
                    GroupBORepository groupBORepository, String selected)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST));
        defineMainMenu(info, selected, model);

        model.addSideMenu("menu.list.tournaments",
                        getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST),
                        getTournamentsSize(tournamentBORepository),
                        getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_ADD),
                        "tournaments".equals(selected));
        model.addSideMenu("menu.list.cashgames",
                        getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_LIST),
                        getCashGamesSize(tournamentBORepository),
                        getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_ADD),
                        "cashgames".equals(selected));
        model.addSideMenu("menu.list.players",
                        getUri(info, PlayerListService.class, PlayerListService.METHOD_NAME_LIST),
                        getPlayersSize(playerBORepository), null, "players".equals(selected));
        model.addSideMenu("menu.list.groups", getUri(info, GroupListService.class, GroupListService.METHOD_NAME_LIST),
                        getGroupsSize(groupBORepository),
                        getUri(info, GroupListService.class, GroupListService.METHOD_NAME_ADD),
                        "groups".equals(selected));
        return model;
    }

    private static void defineMainMenu(UriInfo info, String selected, FrameModel model)
    {
        model.addMainMenu("menu.list.tournaments",
                        getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST),
                        "tournaments".equals(selected));
        model.addMainMenu("menu.list.cashgames",
                        getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_LIST),
                        "cashgames".equals(selected));
        model.addMainMenu("menu.list.players",
                        getUri(info, PlayerListService.class, PlayerListService.METHOD_NAME_LIST),
                        "players".equals(selected));
        model.addMainMenu("menu.list.groups", getUri(info, GroupListService.class, GroupListService.METHOD_NAME_LIST),
                        "groups".equals(selected));
    }

    private static Integer getTournamentsSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getTournaments().size();
    }

    private static Integer getCashGamesSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getCashGames().size();
    }

    private static Integer getPlayersSize(PlayerBORepository playerBORepository)
    {
        return playerBORepository.getPlayers().size();
    }

    private static Integer getGroupsSize(GroupBORepository groupBORepository)
    {
        return groupBORepository.getGroups().size();
    }

    private static FrameModel getCashGameFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, String tournamentID)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_LIST));
        defineMainMenu(info, "cashgames", model);

        CashGameBO cashGameBO = tournamentBORepository.getCashGameByID(tournamentID);
        model.addSideMenu("menu.cashgame.players",
                        getUri(info, CashGameCompetitorService.class, CashGameCompetitorService.METHOD_NAME_LIST,
                                        cashGameBO.getID()),
                        cashGameBO.getCompetitors().size(),
                        getUri(info, CashGameCompetitorService.class, "addPlayer", cashGameBO.getID()),
                        titleKey.equals("title.cashgame.players"));
        model.addSideMenu("menu.cashgame.history",
                        getUri(info, CashGameCompetitorService.class, "history", cashGameBO.getID()),
                        cashGameBO.getHistoryEntries().size(), null, titleKey.equals("title.cashgame.history"));
        return model;
    }

    private static FrameModel getTournamentFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, String tournamentID)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST));
        defineMainMenu(info, "tournaments", model);

        TournamentBO tournamentBO = tournamentBORepository.getTournamentByID(tournamentID);
        model.addSideMenu(
                        "menu.tournament.general", getUri(info, TournamentService.class,
                                        TournamentService.METHOD_NAME_OVERVIEW, tournamentBO.getID()),
                        null, null, titleKey.equals("title.tournament.general"));
        model.addSideMenu("menu.tournament.players",
                        getUri(info, TournamentCompetitorService.class, TournamentCompetitorService.METHOD_NAME_LIST,
                                        tournamentBO.getID()),
                        tournamentBO.getCompetitors().size(),
                        getUri(info, TournamentCompetitorService.class, "addPlayer", tournamentBO.getID()),
                        titleKey.equals("title.list.players"));
        model.addSideMenu("menu.tournament.levels",
                        getUri(info, TournamentBlindLevelService.class, TournamentBlindLevelService.METHOD_NAME_LIST,
                                        tournamentBO.getID()),
                        tournamentBO.getTournamentRounds().size(), null, titleKey.equals("title.list.levels"));
        model.addSideMenu("menu.tournament.ranks",
                        getUri(info, TournamentRankService.class, TournamentRankService.METHOD_NAME_LIST,
                                        tournamentBO.getID()),
                        tournamentBO.getRanks().size(), null, titleKey.equals("title.list.ranks"));
        model.addSideMenu(
                        "menu.tournament.tables", getUri(info, TournamentTableService.class,
                                        TournamentTableService.METHOD_NAME_LIST, tournamentBO.getID()),
                        null, null, titleKey.equals("title.tournament.tables"));
        model.addSideMenu(
                        "menu.tournament.screen", getUri(info, TournamentService.class,
                                        TournamentService.METHOD_NAME_SCREEN, tournamentBO.getID()),
                        null, null, titleKey.equals("title.tournament.screen"));
        return model;
    }

    private static FrameModel getPlayerFrameModel(String content, String titleKey, UriInfo info)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, PlayerListService.class, PlayerListService.METHOD_NAME_LIST));
        defineMainMenu(info, "players", model);
        return model;
    }

    private static FrameModel getGroupFrameModel(String content, String titleKey, UriInfo info)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST));
        defineMainMenu(info, "groups", model);
        return model;
    }

}
