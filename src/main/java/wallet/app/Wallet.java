package wallet.model;

import lombok.NonNull;
import lombok.Value;

@Value
public class Wallet {
    @NonNull
    private final Money balance;
}
