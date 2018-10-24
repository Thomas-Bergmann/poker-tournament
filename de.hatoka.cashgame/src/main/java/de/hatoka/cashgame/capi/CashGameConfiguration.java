package de.hatoka.cashgame.capi;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "de.hatoka.cashgame.internal.persistence" })
@EnableJpaRepositories(basePackages = { "de.hatoka.cashgame.internal.persistence" })
@ComponentScan(basePackages = { "de.hatoka.cashgame.internal" })
public class CashGameConfiguration
{
}
