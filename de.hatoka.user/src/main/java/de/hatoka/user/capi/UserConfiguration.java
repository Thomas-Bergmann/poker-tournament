package de.hatoka.user.capi;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "de.hatoka.user.internal.persistence" })
@EnableJpaRepositories(basePackages = { "de.hatoka.user.internal.persistence" })
@ComponentScan(basePackages = { "de.hatoka.user.internal" })
public class UserConfiguration
{
}
