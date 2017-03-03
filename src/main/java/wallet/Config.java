package wallet;

import org.postgresql.ds.PGSimpleDataSource;
import wallet.infra.LiquibaseLoader;
import wallet.infra.WalletRepoImpl;
import wallet.model.WalletRepo;

import javax.inject.Singleton;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class Config {

    private static final String DATASOURCE_PASSWORD = "datasource.password";
    private static final String DATASOURCE_USERNAME = "datasource.username";

    @Singleton
    public Map<String, String> env() {
        return System.getenv();
    }

    @Singleton
    public Properties properties(Map<String, String> env) {
        Properties properties = new Properties();

        properties.putAll(System.getProperties());

        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        env.forEach((k, v) -> {
            properties.put(k.replaceAll("_", ".").toLowerCase(), v);
        });

        return properties;
    }

    @Singleton
    public DataSource dataSource(Properties properties) {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(properties.getProperty("datasource.url"));
        String username = properties.getProperty(DATASOURCE_USERNAME);
        if (username != null) {
            ds.setUser(username);
        }
        String password = properties.getProperty(DATASOURCE_PASSWORD);
        if (password != null) {
            ds.setPassword(password);
        }
        return ds;
    }

    @Singleton
    public LiquibaseLoader liquibaseLoader(DataSource dataSource) {
        return new LiquibaseLoader(dataSource);
    }

    public WalletRepo walletRepo(DataSource dataSource, LiquibaseLoader liquibaseLoader) {
        return new WalletRepoImpl(dataSource, liquibaseLoader);
    }
}
