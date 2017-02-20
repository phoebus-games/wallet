package wallet.model;

import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class Money {
    @NonNull
    private final BigDecimal amount;

    public Money(int amount) {
        this.amount = BigDecimal.valueOf(amount);
    }
}
