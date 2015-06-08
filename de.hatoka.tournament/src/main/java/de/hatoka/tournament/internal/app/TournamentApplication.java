package de.hatoka.tournament.internal.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.hatoka.address.internal.modules.AddressBusinessModule;
import de.hatoka.address.internal.modules.AddressDaoModule;
import de.hatoka.common.capi.app.servlet.ResourceService;
import de.hatoka.common.capi.app.servlet.ServletConstants;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.common.capi.modules.JpaDBModule;
import de.hatoka.tournament.internal.app.servlets.CashGameCompetitorService;
import de.hatoka.tournament.internal.app.servlets.CashGameListService;
import de.hatoka.tournament.internal.app.servlets.TournamentBlindLevelService;
import de.hatoka.tournament.internal.app.servlets.TournamentCompetitorService;
import de.hatoka.tournament.internal.app.servlets.TournamentListService;
import de.hatoka.tournament.internal.app.servlets.TournamentRankService;
import de.hatoka.tournament.internal.modules.TournamentBusinessModule;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

/**
 * Registers all request resources
 */
@ApplicationPath("/tournament")
public class TournamentApplication extends Application
{
    private Injector injector;

    public TournamentApplication()
    {
        injector = Guice.createInjector(new CommonDaoModule(), new AddressDaoModule(), new AddressBusinessModule(),
                        new TournamentDaoJpaModule(), new TournamentBusinessModule(), new JpaDBModule("TournamentPU"));
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
        result.add(TournamentCompetitorService.class);
        result.add(TournamentBlindLevelService.class);
        result.add(TournamentRankService.class);
        result.add(CashGameListService.class);
        result.add(CashGameCompetitorService.class);
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