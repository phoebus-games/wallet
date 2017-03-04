package wallet.infra;

import wallet.app.definitions.Error;
import wallet.model.NotEnoughFundsException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@Produces({"application/json"})
public class NotEnoughFundsExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<NotEnoughFundsException> {
    @Override
    public Response toResponse(NotEnoughFundsException exception) {
        return Response
                .status(403)
                .entity(new Error(exception.getMessage()))
                .build();
    }
}
