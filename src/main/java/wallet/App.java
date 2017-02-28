package wallet;

import dagger.ObjectGraph;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import wallet.app.DefaultApi;
import wallet.app.IdApi;
import wallet.infra.AuthFilter;
import wallet.model.WalletRepo;

import java.net.URI;

public class App extends ResourceConfig {
    private final ObjectGraph objectGraph = ObjectGraph.create(new Config());
    private class DaggerFactory<T> implements Factory<T> {
        private final Class<T> klass;

        private DaggerFactory(Class<T> klass) {
            this.klass = klass;
        }

        @Override
        public T provide() {
            return objectGraph.get(klass);
        }

        @Override
        public void dispose(T instance) {

        }
    }
    public App() {
        super(
                DefaultApi.class,
                IdApi.class,
                AuthFilter.class
        );

        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(new DaggerFactory<>(WalletRepo.class)).to(WalletRepo.class);
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
