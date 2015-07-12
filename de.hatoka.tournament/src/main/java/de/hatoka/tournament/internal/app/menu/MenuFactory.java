package de.hatoka.tournament.internal.app.menu;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import de.hatoka.tournament.capi.business.CashGameBO;
import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.servlets.CashGameCompetitorService;
import de.hatoka.tournament.internal.app.servlets.CashGameListService;
import de.hatoka.tournament.internal.app.servlets.TournamentBlindLevelService;
import de.hatoka.tournament.internal.app.servlets.TournamentCompetitorService;
import de.hatoka.tournament.internal.app.servlets.TournamentListService;
import de.hatoka.tournament.internal.app.servlets.TournamentRankService;
import de.hatoka.tournament.internal.app.servlets.TournamentService;

public class MenuFactory
{
    private static final String METHOD_NAME_LIST = "list";

    private URI getUri(UriInfo info, Class<?> resource, String methodName, Object... values)
    {
        return info.getBaseUriBuilder().path(resource).path(resource, methodName).build(values);
    }

    public FrameModel getMainFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, boolean isCashGame)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, isCashGame ? CashGameListService.class : TournamentListService.class, METHOD_NAME_LIST));
        defineMainMenu(info, isCashGame, model);

        model.addSideMenu("menu.list.tournaments", getUri(info, TournamentListService.class, METHOD_NAME_LIST),
                        getTournamentsSize(tournamentBORepository), getUri(info, TournamentListService.class, "add"),
                        !isCashGame);
        model.addSideMenu("menu.list.cashgames", getUri(info, CashGameListService.class, METHOD_NAME_LIST),
                        getCashGamesSize(tournamentBORepository), getUri(info, CashGameListService.class, "add"),
                        isCashGame);
        return model;
    }

    private void defineMainMenu(UriInfo info, boolean isCashGame, FrameModel model)
    {
        model.addMainMenu("menu.list.tournaments", getUri(info, TournamentListService.class, METHOD_NAME_LIST), !isCashGame);
        model.addMainMenu("menu.list.cashgames", getUri(info, CashGameListService.class, METHOD_NAME_LIST), isCashGame);
    }

    private Integer getTournamentsSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getTournaments().size();
    }

    private Integer getCashGamesSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getCashGames().size();
    }

    public FrameModel getCashGameFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, String tournamentID)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, CashGameListService.class, METHOD_NAME_LIST));
        defineMainMenu(info, true, model);

        CashGameBO cashGameBO = tournamentBORepository.getCashGameByID(tournamentID);
        model.addSideMenu("menu.cashgame.players",
                        getUri(info, CashGameCompetitorService.class, METHOD_NAME_LIST, cashGameBO.getID()), cashGameBO
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
        model.setUriHome(getUri(info, TournamentListService.class, METHOD_NAME_LIST));
        defineMainMenu(info, false, model);

        TournamentBO tournamentBO = tournamentBORepository.getTournamentByID(tournamentID);
        model.addSideMenu("menu.tournament.general",
                        getUri(info, TournamentService.class, METHOD_NAME_LIST, tournamentBO.getID()), null, null, titleKey.equals("title.tournament.general"));
        model.addSideMenu("menu.tournament.players",
                        getUri(info, TournamentCompetitorService.class, METHOD_NAME_LIST, tournamentBO.getID()), tournamentBO
                                        .getCompetitors().size(),
                        getUri(info, TournamentCompetitorService.class, "addPlayer", tournamentBO.getID()), titleKey.equals("title.list.players"));
        model.addSideMenu("menu.tournament.levels",
                        getUri(info, TournamentBlindLevelService.class, METHOD_NAME_LIST, tournamentBO.getID()), tournamentBO
                                        .getBlindLevels().size(), null, titleKey.equals("title.list.levels"));
        model.addSideMenu("menu.tournament.ranks",
                        getUri(info, TournamentRankService.class, METHOD_NAME_LIST, tournamentBO.getID()), tournamentBO
                                        .getRanks().size(), null, titleKey.equals("title.list.ranks"));
        return model;
    }

}
