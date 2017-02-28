package wallet;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;

import java.io.IOException;
import java.util.Map;

public class App extends PackagesResourceConfig {

    public static void main(String[] args) throws IOException {
        GrizzlyServerFactory.createHttpServer("http://localhost:8080", new App()).start();
    }

    public App() {
        super("wallet");
        Map<String, Boolean> features = getFeatures();
        features.put("com.sun.jersey.api.json.POJOMappingFeature", true);
        Map<String, Object> properties = getProperties();
        properties.put("com.sun.jersey.spi.container.ContainerRequestFilters", "wallet.infra.AuthFilter");
    }
}
