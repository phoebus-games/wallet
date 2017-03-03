package wallet;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import wallet.app.DefaultApi;
import wallet.app.IdApi;
import wallet.infra.AuthFilter;
import wallet.infra.LiquibaseLoader;
import wallet.infra.WalletRepoImpl;
import wallet.model.WalletRepo;

import javax.sql.DataSource;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

public class App extends ResourceConfig {

    public App() {
        super(
                DefaultApi.class,
                IdApi.class,
                AuthFilter.class
        );

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(new Factory<WalletRepo>() {
                    private final Config config = new Config();
                    private final Map<String, String> env = config.env();
                    private final Properties properties = config.properties(env);
                    private final DataSource dataSource = config.dataSource(properties);
                    private final LiquibaseLoader liquibaseLoader = new LiquibaseLoader(dataSource);

                    @Override
                    public WalletRepo provide() {
                        return new WalletRepoImpl(dataSource, liquibaseLoader);
                    }

                    @Override
                    public void dispose(WalletRepo walletRepo) {

                    }
                }).to(WalletRepo.class);
            }
        });

    }

    public static void main(String[] args) throws Exception {
        String url = "http://0.0.0.0:" + System.getProperty("server.port", "8080");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(url), new App());
        server.start();
        Thread.currentThread().join();
        server.shutdown();

    }
}
