package wallet.infra;

import wallet.app.definitions.Error;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Produces({"application/json"})
public class IllegalArgumentExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response
                .status(400)
                .entity(new Error(exception.getMessage()))
                .build();
    }
}
