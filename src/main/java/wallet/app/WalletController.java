package wallet.app;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wallet.model.Wallet;

import java.math.BigDecimal;

@RestController
@RequestMapping("/")
public class WalletController {

    @Data
    public static class WalletDao {
        private BigDecimal balance;

        @SuppressWarnings("unused")
        public WalletDao() {
        }

        WalletDao(BigDecimal balance) {
            this.balance = balance;
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Data
    public static class TransactionDao {
        private BigDecimal amount;
    }

    private final Wallet wallet = new Wallet(new BigDecimal(0));

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@RequestBody WalletDao wallet) {
        this.wallet.setBalance(wallet.getBalance());
    }

    @GetMapping
    public WalletDao get() {
        return new WalletDao(wallet.getBalance());
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public WalletDao createTransaction(@RequestBody TransactionDao transaction) {
        wallet.createTransaction(transaction.getAmount());
        return get();
    }
}
