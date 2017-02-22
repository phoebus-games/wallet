package wallet;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import wallet.app.WalletController;
import wallet.infra.WalletRepo;

@Configuration
@EnableAutoConfiguration
//@EnableTransactionManagement
//@EnableJpaRepositories("wallet.infra")
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
        LiquibaseAutoConfiguration.class
})
public class Config {

    @Bean
    public WalletRepo walletRepo(JdbcTemplate template) {
        return new WalletRepo(template) ;
    }
    @Bean
    public WalletController walletController(WalletRepo repo) {
        return new WalletController(repo);
    }
}
