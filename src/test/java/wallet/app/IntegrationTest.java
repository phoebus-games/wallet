package wallet.app;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import wallet.App;

import java.io.IOException;

abstract class IntegrationTest {
    private final App app = new App();
    private final HttpServer server = GrizzlyServerFactory.createHttpServer("http://localhost:8080", new App());

    IntegrationTest() throws IOException {
    }

    private static PreemptiveBasicAuthScheme auth() {
        PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
        auth.setUserName("roulette");
        auth.setPassword("roulette");
        return auth;
    }

    @Before
    public void setUp() throws Exception {
        RestAssured.reset();
        RestAssured.authentication = auth();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }
}
