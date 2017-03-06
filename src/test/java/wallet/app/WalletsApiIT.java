package wallet.app;

import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class WalletsApiIT extends IntegrationTest {
    private String wallet;

    public WalletsApiIT() throws IOException {
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        wallet = given()
                .contentType(ContentType.JSON)
                .when()
                .post("/wallets")
                .then()
                .statusCode(201)
                .header("Content-Type", "application/json")
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
                .header("Content-Type", "application/json")
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
    public void routerCanGetWallet() throws Exception {
        given()
                .auth().preemptive().basic("router", "router")
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
                .header("Content-Type", "application/json")
                .body("balance", equalTo(0.0f));
    }

    @Test
    public void createTransaction() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": 200, \"category\": \"WAGER\"}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(201)
                .header("Content-Type", "application/json")
                .body("balance", equalTo(200.0f));

        given()
                .when()
                .get(wallet)
                .then()
                .statusCode(200)
                .header("Content-Type", "application/json")
                .body("balance", equalTo(200.0f));
    }

    @Test
    public void mustHaveAmount() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"category\": \"WAGER\"}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(400)
                .header("Content-Type", "application/json")
                .body("message", equalTo("missing amount"));
    }

    @Test
    public void mustHaveValidAmount() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": 0.001, \"category\": \"WAGER\"}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(400)
                .header("Content-Type", "application/json")
                .body("message", equalTo("invalid amount"));
    }

    @Test
    public void mustHaveCategory() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": -200}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(400)
                .header("Content-Type", "application/json")
                .body("message", equalTo("missing category"));
    }

    @Test
    public void mustHaveValidCategory() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": -200, \"category\": \"XXX\"}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(400);
        // shame we can't enforce the other normal constraints
    }

    @Test
    public void cannotHaveBalanceBelowZero() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body("{\"amount\": -200, \"category\": \"PAYOUT\"}")
                .when()
                .post(wallet + "/transactions")
                .then()
                .statusCode(403)
                .header("Content-Type", "application/json")
                .body("message", equalTo("not enough funds"));
    }
}
