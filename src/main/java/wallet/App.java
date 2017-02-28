package wallet;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;

import java.io.IOException;
import java.util.Map;

public class App extends PackagesResourceConfig {

    public static void main(String[] args) throws IOException, InterruptedException {
        HttpServer server = GrizzlyServerFactory.createHttpServer("http://localhost:8080", new App());
        server.start();
        Thread.currentThread().join();
        server.stop();

    }

    public App() {
        super("wallet");
        Map<String, Boolean> features = getFeatures();
        features.put("com.sun.jersey.api.json.POJOMappingFeature", true);
        Map<String, Object> properties = getProperties();
        properties.put("com.sun.jersey.spi.container.ContainerRequestFilters", "wallet.infra.AuthFilter");
    }
}
