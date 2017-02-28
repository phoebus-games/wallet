package wallet;

import dagger.Module;
import dagger.Provides;
import org.postgresql.ds.PGSimpleDataSource;
import wallet.infra.LiquibaseLoader;
import wallet.infra.WalletRepo;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Module(
        library = true, injects = WalletRepo.class
)
public class Config {

    @Provides
    public Properties properties() {
        Properties properties = new Properties();
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.getenv().forEach((k, v) -> {
            properties.put(k.replaceAll("_", ".").toLowerCase(), v);
        });
        return properties;
    }

    @Provides
    public DataSource dataSource(Properties properties) {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(properties.getProperty("datasource.url"));
        if (properties.contains("datasource.username")) {
            ds.setUser("datasource.username");
        }
        if (properties.contains("datasource.password")) {
            ds.setUser("datasource.password§");
        }
        return ds;
    }

    @Provides
    public LiquibaseLoader liquibaseLoader(DataSource dataSource) {
        return new LiquibaseLoader(dataSource);
    }

    @Provides
    public WalletRepo walletRepo(DataSource dataSource, LiquibaseLoader liquibaseLoader) {
        return new WalletRepo(dataSource, liquibaseLoader);
    }
}
