package de.hatoka.tournament.internal.app.menu;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.PlayerBORepository;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
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

public class MenuFactory
{
    private URI getUri(UriInfo info, Class<?> resource, String methodName, Object... values)
    {
        return info.getBaseUriBuilder().path(resource).path(resource, methodName).build(values);
    }

    public FrameModel getMainFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, PlayerBORepository playerBORepository, String selected)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST));
        defineMainMenu(info, selected, model);

        model.addSideMenu("menu.list.tournaments", getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST),
                        getTournamentsSize(tournamentBORepository), getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_ADD),
                        "tournaments".equals(selected));
        model.addSideMenu("menu.list.cashgames", getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_LIST),
                        getCashGamesSize(tournamentBORepository), getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_ADD),
                        "cashgames".equals(selected));
        model.addSideMenu("menu.list.players", getUri(info, PlayerListService.class, PlayerListService.METHOD_NAME_LIST),
                        getPlayersSize(playerBORepository), null,
                        "players".equals(selected));
        return model;
    }

    private void defineMainMenu(UriInfo info, String main, FrameModel model)
    {
        model.addMainMenu("menu.list.tournaments", getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST), "tournaments".equals(main));
        model.addMainMenu("menu.list.cashgames", getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_LIST), "cashgames".equals(main));
        model.addMainMenu("menu.list.players", getUri(info, PlayerListService.class, CashGameListService.METHOD_NAME_LIST), "players".equals(main));
    }

    private Integer getTournamentsSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getTournaments().size();
    }

    private Integer getCashGamesSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getCashGames().size();
    }

    private Integer getPlayersSize(PlayerBORepository playerBORepository)
    {
        return playerBORepository.getPlayers().size();
    }

    public FrameModel getCashGameFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, String tournamentID)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, CashGameListService.class, CashGameListService.METHOD_NAME_LIST));
        defineMainMenu(info, "cashgames", model);

        CashGameBO cashGameBO = tournamentBORepository.getCashGameByID(tournamentID);
        model.addSideMenu("menu.cashgame.players",
                        getUri(info, CashGameCompetitorService.class, CashGameCompetitorService.METHOD_NAME_LIST, cashGameBO.getID()), cashGameBO
                                        .getCompetitors().size(),
                        getUri(info, CashGameCompetitorService.class, "addPlayer", cashGameBO.getID()), titleKey.equals("title.list.players"));
        model.addSideMenu("menu.cashgame.history",
                        getUri(info, CashGameCompetitorService.class, "history", cashGameBO.getID()), cashGameBO.getHistoryEntries().size(), null, titleKey.equals("title.list.history"));
        return model;
    }

    public FrameModel getTournamentFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, String tournamentID)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST));
        defineMainMenu(info, "tournaments", model);

        TournamentBO tournamentBO = tournamentBORepository.getTournamentByID(tournamentID);
        model.addSideMenu("menu.tournament.general",
                        getUri(info, TournamentService.class, TournamentService.METHOD_NAME_LIST, tournamentBO.getID()), null, null, titleKey.equals("title.tournament.general"));
        model.addSideMenu("menu.tournament.players",
                        getUri(info, TournamentCompetitorService.class, TournamentCompetitorService.METHOD_NAME_LIST, tournamentBO.getID()), tournamentBO
                                        .getCompetitors().size(),
                        getUri(info, TournamentCompetitorService.class, "addPlayer", tournamentBO.getID()), titleKey.equals("title.list.players"));
        model.addSideMenu("menu.tournament.levels",
                        getUri(info, TournamentBlindLevelService.class, TournamentBlindLevelService.METHOD_NAME_LIST, tournamentBO.getID()), tournamentBO
                                        .getTournamentRounds().size(), null, titleKey.equals("title.list.levels"));
        model.addSideMenu("menu.tournament.ranks",
                        getUri(info, TournamentRankService.class, TournamentRankService.METHOD_NAME_LIST, tournamentBO.getID()), tournamentBO
                                        .getRanks().size(), null, titleKey.equals("title.list.ranks"));
        model.addSideMenu("menu.tournament.tables",
                        getUri(info, TournamentTableService.class, TournamentTableService.METHOD_NAME_LIST, tournamentBO.getID()), null, null, titleKey.equals("title.tournament.tables"));
        model.addSideMenu("menu.tournament.screen",
                        getUri(info, TournamentService.class, TournamentService.METHOD_NAME_SCREEN, tournamentBO.getID()), null, null, titleKey.equals("title.tournament.screen"));
        return model;
    }

    public FrameModel getPlayerFrameModel(String content, String titleKey, UriInfo info)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, TournamentListService.class, TournamentListService.METHOD_NAME_LIST));
        defineMainMenu(info, "players", model);
        return model;
    }

}
