package de.hatoka.offlinepoker.internal.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.account.internal.app.servlets.LoginService;
import de.hatoka.account.internal.modules.AccountBusinessModule;
import de.hatoka.account.internal.modules.AccountConfigurationModule;
import de.hatoka.account.internal.modules.AccountDaoJpaModule;
import de.hatoka.common.capi.app.servlet.ResourceService;
import de.hatoka.common.capi.app.servlet.ServletConstants;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.common.capi.modules.CommonDateModule;
import de.hatoka.common.capi.modules.JpaDBModule;
import de.hatoka.mail.internal.modules.MailDaoJpaModule;
import de.hatoka.mail.internal.modules.MailServiceConfigurationModule;
import de.hatoka.mail.internal.modules.MailServiceModule;
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
import de.hatoka.tournament.internal.modules.TournamentBusinessModule;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

/**
 * Registers all request resources
 */
public class OfflinePokerApplication extends Application
{
    private Injector injector;

    public OfflinePokerApplication()
    {
        injector = Guice.createInjector(new CommonDaoModule(), new CommonDateModule(),
                        new AccountDaoJpaModule(), new AccountBusinessModule(),
                        new TournamentDaoJpaModule(), new TournamentBusinessModule(),
                        new MailDaoJpaModule(), new MailServiceModule(), new MailServiceConfigurationModule(), new AccountConfigurationModule(),
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
        return result;
    }

    @Override
    public Map<String, Object> getProperties()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(ServletConstants.PROPERTY_INJECTOR, injector);
        return result;
    }
}