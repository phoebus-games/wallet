package wallet.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Wallet {
    private BigDecimal balance;

    public Wallet(BigDecimal balance) {
        this.balance = Objects.requireNonNull(balance);
    }

    public void createTransaction(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
