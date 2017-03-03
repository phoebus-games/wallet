package wallet.infra;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

public class LiquibaseLoader {

    public LiquibaseLoader(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("classpath:/db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor() {
                @Override
                public Set<InputStream> getResourcesAsStream(String path) throws IOException {
                    return super.getResourcesAsStream(path.substring("classpath:/".length()));
                }
            }, database);
            liquibase.update("");
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
}
