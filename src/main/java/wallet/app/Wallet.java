package wallet.app;

import lombok.NonNull;
import lombok.Value;
import wallet.model.Money;

@Value
public class Wallet {
    @NonNull
    private final Money balance;
}
