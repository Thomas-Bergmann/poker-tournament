package de.hatoka.group.capi;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = { "de.hatoka.group.internal.persistence" })
@EnableJpaRepositories(basePackages = { "de.hatoka.group.internal.persistence" })
@ComponentScan(basePackages = { "de.hatoka.group.internal" })
public class GroupConfiguration
{
}
