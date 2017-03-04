package wallet.app;

import wallet.app.definitions.Error;
import wallet.model.NotEnoughFundsException;
import wallet.model.Transaction;
import wallet.model.WalletRepo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.sql.SQLException;

@Path("/{id}")
public class IdApi {

    @Inject
    public WalletRepo repo;

    @GET
    @Produces({"application/json"})
    public Response getWallet(@PathParam("id") Long id) throws SQLException {
        return Response.ok(repo.findOne(id)).build();
    }

    @POST
    @Path("/transactions")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response createTransaction(@PathParam("id") Long id, Transaction transaction) throws SQLException {
        try {
            repo.createTransaction(id, transaction);
        } catch (NotEnoughFundsException e) {
            return Response.status(403).entity(new Error("not enough funds")).build();
        }
        return Response.created(URI.create(".")).entity(repo.findOne(id)).build();
    }
}
