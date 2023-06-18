package de.hatoka.tournament.capi;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "de.hatoka.tournament.internal.persistence" })
@EnableJpaRepositories(basePackages = { "de.hatoka.tournament.internal.persistence" })
@ComponentScan(basePackages = { "de.hatoka.tournament.internal", "de.hatoka.common" })
public class TournamentConfiguration
{
}
