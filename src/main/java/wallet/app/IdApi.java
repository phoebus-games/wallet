package wallet.app;

import wallet.app.definitions.Error;
import wallet.infra.NotEnoughFundsException;
import wallet.infra.WalletRepo;
import wallet.model.Transaction;
import wallet.model.Wallet;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;
import java.sql.SQLException;

@Path("/{id}")
public class IdApi {

    @Resource
    private WalletRepo repo;

    @GET
    @Produces({"application/json"})
    public Response getWallet(@PathParam("id") Long id) throws SQLException {
        return Response.ok(repo.findOne(id)).build();
    }

    @POST
    @Path("/transactions")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response createTransaction(@PathParam("id") Long id, Transaction transaction, @Context SecurityContext sc) throws SQLException {
        Wallet wallet = repo.findOne(id);
        wallet.updateBalance(transaction.getAmount());
        try {
            repo.save(id, wallet);
        } catch (NotEnoughFundsException e) {
            return Response.status(403).entity(new Error("not enough funds")).build();
        }
        return Response.created(URI.create("")).entity(wallet).build();
    }
}
