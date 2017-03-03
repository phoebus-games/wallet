package wallet.app;

import wallet.model.Wallet;
import wallet.model.WalletRepo;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.SQLException;


@Path("/")
public class DefaultApi {

    @Inject
    public WalletRepo repo;

    @POST
    @Produces({"application/json"})
    public Response createWallet() throws SQLException {
        long id = repo.create();
        return Response
                .created(URI.create(String.valueOf(id)))
                .entity(new Wallet(BigDecimal.ZERO))
                .build();
    }
}
