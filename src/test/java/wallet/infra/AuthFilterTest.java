package wallet.infra;

import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.server.ContainerRequest;
import org.junit.Test;
import wallet.app.definitions.Error;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthFilterTest {

    private final AuthFilter authFilter = new AuthFilter();
    private final ContainerRequestContext context = new ContainerRequest(null, null, "GET", null, new MapPropertiesDelegate());
    private final MultivaluedMap<String, String> headers = context.getHeaders();
    private final String webBearer = "Basic d2ViOndlYg==";
    private final String webBearerBad = "Basic d2ViOmZvbw==";
    private final String rouletteBearer  = "Basic cm91bGV0dGU6cm91bGV0dGU=";

    private static void assertStatus(int status, Runnable block, String message) {
        try {
            block.run();
            fail("expected " + status);
        } catch (WebApplicationException e) {
            assertEquals(status, e.getResponse().getStatus());
            assertErrorPayload(e, message);
        }
    }

    private static void assertErrorPayload(WebApplicationException e, String message) {
        assertEquals("application/json", e.getResponse().getHeaderString("Content-Type"));
        Error error = Error.class.cast(e.getResponse().getEntity());
        assertEquals(message, error.getMessage());
    }

    @Test
    public void assertUnauthorized() throws Exception {
        try {
            authFilter.filter(context);
            fail();
        } catch (WebApplicationException e) {
            assertStatus(401, () -> authFilter.filter(context), "unauthorized");
            assertEquals("Basic", e.getResponse().getHeaderString("WWW-Authenticate"));
            assertErrorPayload(e, "unauthorized");
        }
    }

    @Test
    public void failsIfBadToken() throws Exception {
        authorisation(webBearerBad);
        assertStatus(401, () -> authFilter.filter(context), "unauthorized");
    }

    @Test(expected = Exception.class)
    public void failsIfGarbage() throws Exception {
        authorisation("");
        authFilter.filter(context);
    }

    @Test
    public void webCanGet() throws Exception {
        authorisation(webBearer);
        authFilter.filter(context);
    }

    private void authorisation(String bearer) {
        headers.put("Authorization", Collections.singletonList(bearer));
    }

    @Test
    public void webCannotPost() throws Exception {
        post();
        authorisation(webBearer);
        assertStatus(403, () -> authFilter.filter(context), "forbidden");
    }

    private void post() {
        context.setMethod("POST");
    }

    @Test
    public void webCannotPut() throws Exception {
        put();
        authorisation(webBearer);
        assertStatus(403, () -> authFilter.filter(context), "forbidden");
    }

    private void put() {
        context.setMethod("PUT");
    }

    @Test
    public void webCannotDelete() throws Exception {
        delete();
        authorisation(webBearer);
        assertStatus(403, () -> authFilter.filter(context), "forbidden");
    }

    private void delete() {
        context.setMethod("DELETE");
    }

    @Test
    public void rouletteCanGet() throws Exception {
        authorisation(rouletteBearer);
        authFilter.filter(context);
    }

    @Test
    public void rouletteCanPost() throws Exception {
        post();
        authorisation(rouletteBearer);
        authFilter.filter(context);
    }
}