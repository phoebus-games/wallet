package wallet.model;

import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {
    @NonNull
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        int scale = amount.scale();
        if (scale > 2) {
            throw new IllegalArgumentException("amount + " +amount + " has scale "  +scale + ",  > 2 ");
        }
        this.amount = amount;
    }

    public Money(int amount) {
        this(BigDecimal.valueOf(amount));
    }

    public Money(String amount) {
        this(new BigDecimal(amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return amount.setScale(2, RoundingMode.UNNECESSARY).toString();
    }
}
