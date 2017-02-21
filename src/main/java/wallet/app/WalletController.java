package wallet.app;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wallet.model.Wallet;

import java.math.BigDecimal;

@RestController
@RequestMapping("/")
public class WalletController {

    private final Wallet wallet = new Wallet(new BigDecimal(1000));

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@RequestParam("amount") BigDecimal amount) {
        wallet.setBalance(amount);
    }

    @GetMapping
    public Wallet get() {
        return wallet;
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestParam("amount") BigDecimal amount) {
        wallet.createTransaction(amount);
    }
}
