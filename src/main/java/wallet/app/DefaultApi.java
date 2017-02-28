package wallet.app;

import wallet.infra.WalletRepo;
import wallet.model.Wallet;

import javax.annotation.Resource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.SQLException;


@Path("/")
public class DefaultApi {

    @Resource
    private WalletRepo repo;

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
