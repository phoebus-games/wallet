package wallet;

import dagger.Module;
import dagger.Provides;
import org.postgresql.ds.PGSimpleDataSource;
import wallet.infra.LiquibaseLoader;
import wallet.infra.WalletRepo;

import javax.sql.DataSource;

@Module(
        library = true, injects = WalletRepo.class
)
public class Config {

    @Provides
    public DataSource dataSource() {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(System.getenv().getOrDefault("DATASOURCE_URL", "jdbc:postgresql://localhost:5432/wallet"));
        System.getenv().forEach(
                (key, value) -> {
                    switch (key) {
                        case "DATASOURCE_USERNAME":
                            ds.setUser(value);
                            break;
                        case "DATASOURCE_PASSWORD":
                            ds.setPassword(value);
                            break;
                    }
                }
        );
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
