package wallet.infra;

import com.sun.jersey.core.header.InBoundHeaders;
import com.sun.jersey.server.impl.application.WebApplicationImpl;
import com.sun.jersey.spi.container.ContainerRequest;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AuthFilterTest {

    private final AuthFilter authFilter = new AuthFilter();
    private final InBoundHeaders headers = new InBoundHeaders();
    private final ContainerRequest containerRequest = new ContainerRequest(new WebApplicationImpl(), "GET", null, null, headers, null);
    private final String webBearer = "Basic d2ViOndlYg==";
    private final String webBearerBad = "Basic d2ViOmZvbw==";
    private final String rouletteBearer  = "Basic cm91bGV0dGU6cm91bGV0dGU=";

    @Test
    public void failsIfNoToken() throws Exception {
        assertStatus(401, () -> authFilter.filter(containerRequest));
    }
    @Test
    public void failsIfBadToken() throws Exception {
        authorisation(webBearerBad);
        assertStatus(401, () -> authFilter.filter(containerRequest));
    }
    private static void assertStatus(int status, Runnable block) {
        try {
            block.run();
            fail("expected " + status);
        } catch (WebApplicationException e) {
            assertEquals(status, e.getResponse().getStatus());
        }
    }

    @Test(expected = Exception.class)
    public void failsIfGarbage() throws Exception {
        authorisation("");
        authFilter.filter(containerRequest);
    }

    @Test
    public void webCanGet() throws Exception {
        authorisation(webBearer);
        authFilter.filter(containerRequest);
    }

    private void authorisation(String bearer) {
        headers.put("Authorization", Collections.singletonList(bearer));
    }

    @Test
    public void webCannotPost() throws Exception {
        post();
        authorisation(webBearer);
        assertStatus(403, () -> authFilter.filter(containerRequest));
    }

    private void post() {
        containerRequest.setMethod("POST");
    }

    @Test
    public void webCannotPut() throws Exception {
        put();
        authorisation(webBearer);
        assertStatus(403, () -> authFilter.filter(containerRequest));
    }

    private void put() {
        containerRequest.setMethod("PUT");
    }

    @Test
    public void webCannotDelete() throws Exception {
        delete();
        authorisation(webBearer);
        assertStatus(403, () -> authFilter.filter(containerRequest));
    }

    private void delete() {
        containerRequest.setMethod("DELETE");
    }

    @Test
    public void rouletteCanGet() throws Exception {
        authorisation(rouletteBearer);
        authFilter.filter(containerRequest);
    }

    @Test
    public void rouletteCanPost() throws Exception {
        post();
        authorisation(rouletteBearer);
        authFilter.filter(containerRequest);
    }
}