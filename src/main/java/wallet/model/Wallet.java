package wallet.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Wallet {
    private BigDecimal balance = BigDecimal.ZERO;

    public void incrementBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
