package wallet.model;

import java.math.BigDecimal;

public class Wallet {
    private BigDecimal balance;

    public Wallet(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException();
        }
        this.balance = balance;
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
