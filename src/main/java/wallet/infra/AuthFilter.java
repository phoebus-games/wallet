package wallet.infra;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

public class AuthFilter implements ContainerRequestFilter {
    private static final Map<String, List<String>> ROLES = new HashMap<>();

    static {
        ROLES.put("web", Collections.singletonList("WEB"));
        ROLES.put("roulette", Collections.singletonList("GAME"));
        ROLES.put("classic-slot", Collections.singletonList("GAME"));
    }

    private static final Map<String, String> USERS = new HashMap<>();

    static {
        USERS.put("web", "web");
        USERS.put("roulette", "roulette");
        USERS.put("classic-slot", "classic-slot");
    }

    @Override
    public ContainerRequest filter(ContainerRequest containerRequest) throws WebApplicationException {

        String username = authenticate(containerRequest);
        authorize(username, containerRequest.getMethod());

        return containerRequest;
    }

    private static void authorize(String username, String method) {
        List<String> userRoles = ROLES.get(username);

        switch (method) {
            case "GET":
                if (!userRoles.contains("WEB") && !userRoles.contains("GAME")) {
                    forbidden();
                }
                break;
            default:
                if (!userRoles.contains("GAME")) {
                    forbidden();
                }
                break;
        }
    }

    private static String unauthorised() {
        throw new WebApplicationException(Response.status(401).build());
    }

    private static void forbidden() {
        throw new WebApplicationException(Response.status(403).build());
    }

    private static String authenticate(ContainerRequest containerRequest) {
        String token = new String(Base64.getDecoder().decode(getBearerToken(containerRequest)));
        String username = token.replaceFirst(":.*$", "");
        String password = token.replaceFirst("^.*:", "");
        if (!USERS.containsKey(username) || !USERS.get(username).equals(password)) {
            unauthorised();
        }
        return username;
    }

    private static String getBearerToken(ContainerRequest containerRequest) {
        List<String> authorizations = containerRequest.getRequestHeader("Authorization");

        if (authorizations == null || authorizations.isEmpty()) {
            return unauthorised();
        }

        return authorizations.get(0).replaceFirst("^Basic ", "");
    }

}
