package wallet.app;

import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class WalletControllerIT extends IntegrationTest {
    private String wallet;

    public WalletControllerIT() throws IOException {
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        wallet = given()
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("balance", equalTo(0))
                .extract()
                .header("Location");
    }

    @Test
    public void webCannotCreateTransaction() throws Exception {
        given()
                .auth().preemptive().basic("web", "web")
                .contentType(ContentType.JSON)
                .body("{\"amount\": 200}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(403);
    }

    @Test
    public void webCanGetWallet() throws Exception {
        given()
                .auth().preemptive().basic("web", "web")
                .when()
                .get(wallet)
                .then()
                .statusCode(200);
    }

    @Test
    public void canGetWallet() throws Exception {
        given()
                .when()
                .get(wallet)
                .then()
                .statusCode(200)
                .body("balance", equalTo(0.0f));
    }

    @Test
    public void createTransaction() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": 200}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(201)
                .body("balance", equalTo(200.0f));

        given()
                .when()
                .get(wallet)
                .then()
                .statusCode(200)
                .body("balance", equalTo(200.0f));
    }

    @Test
    public void cannotHaveBalanceBelowZero() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": -200}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(403)
                .body("message", equalTo("not enough funds"));
    }
}
