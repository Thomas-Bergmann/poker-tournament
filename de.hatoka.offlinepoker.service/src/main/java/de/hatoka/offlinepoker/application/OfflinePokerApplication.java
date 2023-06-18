package de.hatoka.offlinepoker.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import de.hatoka.cashgame.capi.CashGameConfiguration;
import de.hatoka.group.capi.GroupConfiguration;
import de.hatoka.player.capi.PlayerConfiguration;
import de.hatoka.tournament.capi.TournamentConfiguration;
import de.hatoka.user.capi.UserConfiguration;

@SpringBootApplication
@ComponentScan
@Import(value = { UserConfiguration.class, GroupConfiguration.class, PlayerConfiguration.class, CashGameConfiguration.class, TournamentConfiguration.class })
public class OfflinePokerApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(OfflinePokerApplication.class);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(OfflinePokerApplication.class, args);
    }
}