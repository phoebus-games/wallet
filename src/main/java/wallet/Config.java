package wallet;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import wallet.app.WalletController;
import wallet.infra.WalletRepo;

@Configuration
@EnableAutoConfiguration
@Import({
        DispatcherServletAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
        JacksonAutoConfiguration.class,
        ServerPropertiesAutoConfiguration.class,
        PropertyPlaceholderAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        LiquibaseAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
public class Config {

    @Bean
    public WalletRepo walletRepo(JdbcTemplate template) {
        return new WalletRepo(template);
    }

    @Bean
    public WalletController walletController(WalletRepo repo) {
        return new WalletController(repo);
    }

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapter() {

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http.cors().disable();
                http.csrf().disable();

                http.httpBasic();

                http.authorizeRequests()
                        .antMatchers(HttpMethod.GET).hasAnyRole("WEB", "GAME")
                        .antMatchers(HttpMethod.POST).hasRole("GAME")

                ;
            }

            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth
                        .inMemoryAuthentication()
                        .withUser("web").password("web").roles("WEB").and()
                        .withUser("roulette").password("roulette").roles("GAME").and()
                        .withUser("classic-slot").password("classic-slot").roles("GAME")
                ;
            }
        };
    }
}
