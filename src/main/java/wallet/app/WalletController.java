package wallet.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wallet.model.Money;

@RestController
@RequestMapping("/")
public class WalletController {

    @GetMapping
    public Wallet get() {
        return new Wallet(new Money(1000));
    }
}
