package wallet.app;

import io.restassured.RestAssured;
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

    @Before
    public void setUp() throws Exception {
        RestAssured.reset();
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.basic("roulette", "roulette");
    }
}
