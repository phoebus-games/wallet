package wallet;

import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import wallet.app.DefaultApi;
import wallet.app.IdApi;
import wallet.infra.AuthFilter;
import wallet.infra.DaggerProvider;

import java.io.IOException;

public class App extends DefaultResourceConfig {

    public App() {
        super(
                DefaultApi.class,
                IdApi.class,
                DaggerProvider.class
        );

        getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        getProperties().put("com.sun.jersey.spi.container.ContainerRequestFilters",
                LoggingFilter.class.getName() + ";" +
                AuthFilter.class.getName());
        getProperties().put("com.sun.jersey.spi.container.ContainerResponseFilters",
                LoggingFilter.class.getName());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer server = GrizzlyServerFactory.createHttpServer("http://0.0.0.0:" + System.getProperty("server.port", "8080"), new App());
        server.start();
        Thread.currentThread().join();
        server.stop();

    }
}
