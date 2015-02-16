package de.hatoka.tournament.capi.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.hatoka.address.internal.modules.AddressBusinessModule;
import de.hatoka.address.internal.modules.AddressDaoModule;
import de.hatoka.common.capi.modules.CommonDaoModule;
import de.hatoka.tournament.internal.modules.TournamentBusinessModule;
import de.hatoka.tournament.internal.modules.TournamentDaoJpaModule;

public final class TestBusinessInjectorProvider
{
    public static Injector get(Module... modules)
    {
        List<Module> list = new ArrayList<>(Arrays.asList(modules));
        list.addAll(Arrays.asList(new CommonDaoModule(), new AddressDaoModule(), new TournamentDaoJpaModule(),
                        new TournamentBusinessModule(), new AddressBusinessModule()));
        return Guice.createInjector(list);
    }

    private TestBusinessInjectorProvider()
    {
    }

}
