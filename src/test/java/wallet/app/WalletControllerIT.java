package wallet.app;

import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class WalletControllerIT extends IntegrationTest {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        given()
                .param("amount", "1000")
                .when()
                .put()
                .then()
                .statusCode(204);
    }

    @Test
    public void canGetWallet() throws Exception {
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("balance", equalTo(1000));

    }

    @Test
    public void createTransaction() throws Exception {
        given()
                .param("amount", 10)
                .when()
                .post("/transactions")
                .then()
                .statusCode(201);
    }
}