package wallet.app;

import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import wallet.Config;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Config.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class IntegrationTest {
    @LocalServerPort
    private int port;

    private static PreemptiveBasicAuthScheme auth() {
        PreemptiveBasicAuthScheme auth = new PreemptiveBasicAuthScheme();
        auth.setUserName("roulette");
        auth.setPassword("roulette");
        return auth;
    }

    @Before
    public void setUp() throws Exception {
        RestAssured.reset();
        RestAssured.port = port;
        RestAssured.authentication = auth();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
