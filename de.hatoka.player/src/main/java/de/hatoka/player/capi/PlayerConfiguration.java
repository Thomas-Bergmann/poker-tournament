package de.hatoka.player.capi;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "de.hatoka.player.internal.persistence" })
@EnableJpaRepositories(basePackages = { "de.hatoka.player.internal.persistence" })
@ComponentScan(basePackages = { "de.hatoka.player.internal" })
public class PlayerConfiguration
{
}
