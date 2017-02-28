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
        ds.setDatabaseName("wallet");
        return ds;
    }

    @Provides
    public LiquibaseLoader liquiStrap(DataSource dataSource) {
        return new LiquibaseLoader(dataSource);
    }

    @Provides
    public WalletRepo walletRepo(DataSource dataSource, LiquibaseLoader liquibaseLoader) {
        return new WalletRepo(dataSource, liquibaseLoader);
    }
}
