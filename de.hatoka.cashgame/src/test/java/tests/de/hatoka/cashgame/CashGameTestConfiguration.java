package tests.de.hatoka.cashgame;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import de.hatoka.cashgame.capi.CashGameConfiguration;
import de.hatoka.common.capi.CommonConfiguration;
import de.hatoka.player.capi.PlayerConfiguration;
import de.hatoka.user.capi.UserConfiguration;

@Configuration
@PropertySource("classpath:application-test.properties")
@EnableAutoConfiguration
@ImportAutoConfiguration({ CashGameConfiguration.class, PlayerConfiguration.class, UserConfiguration.class, CommonConfiguration.class })
public class CashGameTestConfiguration
{
}
