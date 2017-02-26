package wallet.app;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wallet.app.api.DefaultApi;
import wallet.app.api.IdApi;
import wallet.infra.WalletRepo;
import wallet.model.Wallet;

import java.math.BigDecimal;
import java.net.URI;

@RestController
public class WalletController implements DefaultApi, IdApi {

    private final WalletRepo repo;

    public WalletController(WalletRepo repo) {
        this.repo = repo;
    }

    @Override
    public ResponseEntity<wallet.app.definitions.Wallet> createWallet() {
        long id = repo.create();
        wallet.app.definitions.Wallet wallet = new wallet.app.definitions.Wallet();
        wallet.setBalance(BigDecimal.ZERO);
        return ResponseEntity.created(URI.create(String.valueOf(id))).body(wallet);
    }

    @Override
    public ResponseEntity<Void> updateWallet(@PathVariable("id") Long id, @RequestBody wallet.app.definitions.Wallet wallet) {
        repo.save(id, new Wallet());
        return ResponseEntity.<Void>noContent().build();
    }

    @Override
    public ResponseEntity<wallet.app.definitions.Wallet> getWallet(@PathVariable("id") Long id) {
        Wallet model = repo.findOne(id);
        wallet.app.definitions.Wallet api = new wallet.app.definitions.Wallet();
        api.setBalance(model.getBalance());
        return ResponseEntity.ok(api);
    }

    @Override
    public ResponseEntity<wallet.app.definitions.Wallet> createTransaction(@PathVariable("id") Long id, @RequestBody wallet.app.definitions.Transaction transaction) {
        Wallet wallet = repo.findOne(id);
        wallet.incrementBalance(transaction.getAmount());
        try {
            repo.save(id, wallet);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("chk_balance")) {
                throw new NotEnoughFundsException();
            } else {
                throw e;
            }
        }
        wallet.app.definitions.Wallet api = new wallet.app.definitions.Wallet();
        api.setBalance(wallet.getBalance());
        return ResponseEntity.created(URI.create("")).body(api);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "not enough funds")
    private static class NotEnoughFundsException extends RuntimeException {

    }
}
