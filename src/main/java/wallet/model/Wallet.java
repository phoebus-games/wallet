package wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {
    private BigDecimal balance;

    public void updateBalance(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
