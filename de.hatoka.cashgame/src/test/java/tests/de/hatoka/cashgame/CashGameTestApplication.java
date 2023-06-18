package tests.de.hatoka.cashgame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class CashGameTestApplication extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(CashGameTestApplication.class);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(CashGameTestApplication.class, args);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.anonymous();
        // https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html
        http.csrf().disable(); // REST requests doesn't use CSRF
        return http.build();
    }
}