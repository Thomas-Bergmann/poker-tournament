package tests.de.hatoka.user;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import de.hatoka.common.capi.CommonConfiguration;
import de.hatoka.user.capi.UserConfiguration;

@Configuration
@EnableAutoConfiguration
@ImportAutoConfiguration({ UserConfiguration.class, CommonConfiguration.class })
public class UserTestConfiguration
{
}
