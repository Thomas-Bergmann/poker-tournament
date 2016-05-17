package de.hatoka.offlinepoker.internal.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.common.capi.app.servlet.ResourceService;
import de.hatoka.common.capi.app.servlet.ServletConstants;
import de.hatoka.common.capi.modules.CommonConfigurationModule;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.common.capi.modules.CommonDateModule;
import de.hatoka.common.capi.modules.JpaDBModule;
import de.hatoka.group.internal.app.servlets.GroupListService;
import de.hatoka.group.modules.GroupBusinessModule;
import de.hatoka.group.modules.GroupConfigurationModule;
import de.hatoka.group.modules.GroupDaoJpaModule;
import de.hatoka.mail.internal.modules.MailConfigurationModule;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.mail.internal.modules.MailServiceModule;
import de.hatoka.offlinepoker.internal.modules.FrameModule;
import de.hatoka.tournament.internal.app.filter.AccountRequestFilter;
import de.hatoka.tournament.internal.app.servlets.CashGameCompetitorService;
import de.hatoka.tournament.internal.app.servlets.CashGameListService;
import de.hatoka.tournament.internal.app.servlets.PlayerListService;
import de.hatoka.tournament.internal.app.servlets.TournamentBlindLevelService;
import de.hatoka.tournament.internal.app.servlets.TournamentCompetitorService;
import de.hatoka.tournament.internal.app.servlets.TournamentListService;
import de.hatoka.tournament.internal.app.servlets.TournamentRankService;
import de.hatoka.tournament.internal.app.servlets.TournamentService;
import de.hatoka.tournament.internal.app.servlets.TournamentTableService;
import de.hatoka.tournament.modules.TournamentBusinessModule;
import de.hatoka.tournament.modules.TournamentConfigurationModule;
import de.hatoka.tournament.modules.TournamentDaoJpaModule;
import de.hatoka.user.internal.app.servlets.LoginService;
import de.hatoka.user.internal.modules.UserBusinessModule;
import de.hatoka.user.internal.modules.UserConfigurationModule;
import de.hatoka.user.internal.modules.UserDaoJpaModule;

/**
 * Registers all request resources
 */
public class OfflinePokerApplication extends Application
{
    private Injector injector;

    public OfflinePokerApplication()
    {
        injector = Guice.createInjector(
                        new CommonDaoModule(), new CommonDateModule(), new CommonConfigurationModule(),
                        new UserDaoJpaModule(), new UserBusinessModule(), new UserConfigurationModule(),
                        new TournamentDaoJpaModule(), new TournamentBusinessModule(), new TournamentConfigurationModule(),
                        new GroupDaoJpaModule(), new GroupBusinessModule(), new GroupConfigurationModule(),
                        new MailDaoJpaModule(), new MailServiceModule(), new MailConfigurationModule(),
                        new JpaDBModule("OfflinePokerPU"));
    }

    @Override
    protected void finalize() throws Throwable
    {
        injector = null;
        super.finalize();
    }

    @Override
    public Set<Class<?>> getClasses()
    {
        final Set<Class<?>> result = new HashSet<>();
        result.add(ResourceService.class);
        result.add(TournamentListService.class);
        result.add(TournamentService.class);
        result.add(TournamentCompetitorService.class);
        result.add(TournamentBlindLevelService.class);
        result.add(TournamentRankService.class);
        result.add(TournamentTableService.class);
        result.add(CashGameListService.class);
        result.add(CashGameCompetitorService.class);
        result.add(PlayerListService.class);
        result.add(AccountRequestFilter.class);
        result.add(LoginService.class);
        result.add(GroupListService.class);
        return result;
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(ServletConstants.PROPERTY_INJECTOR, injector);
        result.put(ServletConstants.PROPERTY_REQUESTMODULES, Arrays.asList(new FrameModule()));
        return result;
    }
}