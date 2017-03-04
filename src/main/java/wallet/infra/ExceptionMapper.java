package wallet.infra;

import wallet.app.definitions.Error;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
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
