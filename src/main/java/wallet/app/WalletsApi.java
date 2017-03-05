package wallet.app;


import wallet.model.Transaction;
import wallet.model.Wallet;
import wallet.model.WalletRepo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.SQLException;

@Path("/wallets")
public class WalletsApi {

    @Inject
    public WalletRepo repo;

    @POST
    @Path("/{id}/transactions")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response createTransaction(@PathParam("id") Long id, Transaction transaction) throws SQLException {
        repo.createTransaction(id, transaction);
        return Response.created(URI.create(".")).entity(repo.findOne(id)).build();
    }

    @POST
    @Produces({"application/json"})
    public Response createWallet() throws SQLException {
        long id = repo.create();
        return Response
                .created(URI.create("/wallets/" + id))
                .entity(new Wallet(BigDecimal.ZERO))
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces({"application/json"})
    public Response getWallet(@PathParam("id") Long id) throws SQLException {
        return Response.ok(repo.findOne(id)).build();
    }
}

