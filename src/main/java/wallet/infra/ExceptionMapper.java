package wallet.infra;

import wallet.app.definitions.Error;

import javax.annotation.Priority;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(0)
@Produces({"application/json"})
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable exception) {
        exception.printStackTrace();
        return Response
                .serverError()
                .entity(new Error(exception.getMessage()))
                .build();
    }
}
