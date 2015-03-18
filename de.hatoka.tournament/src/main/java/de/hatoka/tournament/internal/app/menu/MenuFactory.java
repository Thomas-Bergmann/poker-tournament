package de.hatoka.tournament.internal.app.menu;

import java.net.URI;

import javax.ws.rs.core.UriInfo;

import de.hatoka.tournament.capi.business.TournamentBO;
import de.hatoka.tournament.capi.business.TournamentBORepository;
import de.hatoka.tournament.internal.app.models.FrameModel;
import de.hatoka.tournament.internal.app.models.MenuItemVO;
import de.hatoka.tournament.internal.app.servlets.CashGameCompetitorService;
import de.hatoka.tournament.internal.app.servlets.CashGameListService;
import de.hatoka.tournament.internal.app.servlets.TournamentCompetitorService;
import de.hatoka.tournament.internal.app.servlets.TournamentListService;

public class MenuFactory
{
    private URI getUri(UriInfo info, Class<?> resource, String methodName, Object... values)
    {
        return info.getBaseUriBuilder().path(resource).path(resource, methodName).build(values);
    }

    public FrameModel getMainFrameModel(String content, String titleKey, UriInfo info, TournamentBORepository tournamentBORepository, boolean isCashGame)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, isCashGame ? CashGameListService.class : TournamentListService.class, "list"));
        model.addMainMenu("menu.list.tournaments", getUri(info, TournamentListService.class, "list"), !isCashGame);
        model.addMainMenu("menu.list.cashgames", getUri(info, CashGameListService.class, "list"), isCashGame);

        model.addSideMenu("menu.list.tournaments", getUri(info, TournamentListService.class, "list"), getTournamentsSize(tournamentBORepository),
                        getUri(info, TournamentListService.class, "add"), !isCashGame);
        model.addSideMenu("menu.list.cashgames", getUri(info, CashGameListService.class, "list"), getCashGamesSize(tournamentBORepository),
                        getUri(info, CashGameListService.class, "add"), isCashGame);
        return model;
    }

    private Integer getTournamentsSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getTournamenBOs().size();
    }

    private Integer getCashGamesSize(TournamentBORepository tournamentBORepository)
    {
        return tournamentBORepository.getCashGameBOs().size();
    }

    public FrameModel getCashGameFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, String tournamentID)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, CashGameListService.class, "list"));
        model.addMainMenu("menu.list.cashgames", getUri(info, CashGameListService.class, "list"), false);
        TournamentBO tournamentBO = tournamentBORepository.getByID(tournamentID);
        MenuItemVO gameMenu = model.addMainMenu("menu.list.cashgames", getUri(info, CashGameCompetitorService.class, "players", tournamentBO.getID()), true);
        gameMenu.setTitle(tournamentBO.getName());
        model.addSideMenu("menu.list.cashgames", getUri(info, CashGameListService.class, "list"), getCashGamesSize(tournamentBORepository),
                        getUri(info, CashGameListService.class, "add"), false);
        model.addSideMenu("menu.cashgame.players", getUri(info, CashGameCompetitorService.class, "players", tournamentBO.getID()), tournamentBO.getCompetitors().size(), getUri(info, CashGameCompetitorService.class, "addPlayer", tournamentBO.getID()), true);
        return model;
    }

    public FrameModel getTournamentFrameModel(String content, String titleKey, UriInfo info,
                    TournamentBORepository tournamentBORepository, String tournamentID)
    {
        FrameModel model = new FrameModel();
        model.setTitleKey(titleKey);
        model.setContent(content);
        model.setUriHome(getUri(info, TournamentListService.class, "list"));
        model.addMainMenu("menu.list.tournaments", getUri(info, TournamentListService.class, "list"), false);
        TournamentBO tournamentBO = tournamentBORepository.getByID(tournamentID);
        MenuItemVO gameMenu = model.addMainMenu("menu.list.cashgames", getUri(info, TournamentCompetitorService.class, "players", tournamentBO.getID()), true);
        gameMenu.setTitle(tournamentBO.getName());
        model.addSideMenu("menu.list.tournaments", getUri(info, TournamentListService.class, "list"), getTournamentsSize(tournamentBORepository),
                        getUri(info, TournamentListService.class, "add"), false);
        model.addSideMenu("menu.tournament.players", getUri(info, TournamentCompetitorService.class, "players", tournamentBO.getID()), tournamentBO.getCompetitors().size(), getUri(info, TournamentCompetitorService.class, "addPlayer", tournamentBO.getID()), true);
        return model;
    }


}
