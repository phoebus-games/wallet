package wallet.app;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class WalletControllerIT extends IntegrationTest {

    @Test
    public void canGetWallet() throws Exception {
        given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("balance", equalTo("1000"));

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