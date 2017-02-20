package wallet.app;

import lombok.NonNull;
import lombok.Value;
import wallet.model.Money;

@Value
class Wallet {
    @NonNull
    private final Money balance;
}
