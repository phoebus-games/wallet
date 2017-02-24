package wallet.app;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wallet.infra.WalletRepo;
import wallet.model.Transaction;
import wallet.model.Wallet;

import java.net.URI;

@RestController
@RequestMapping("/")
public class WalletController {

    private final WalletRepo repo;

    public WalletController(WalletRepo repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet() {
        long id = repo.create();
        return ResponseEntity.created(URI.create(String.valueOf(id))).body(new Wallet());
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWallet(@PathVariable("id") long id, @RequestBody Wallet wallet) {
        repo.save(id, wallet);
    }

    @GetMapping("{id}")
    public Wallet getWallet(@PathVariable("id") long id) {
        return repo.findOne(id);
    }

    @PostMapping("{id}/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public Wallet createTransaction(@PathVariable("id") long id, @RequestBody Transaction transaction) {
        Wallet wallet = getWallet(id);
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
        return wallet;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "not enough funds")
    public static class NotEnoughFundsException extends RuntimeException {

    }
}
