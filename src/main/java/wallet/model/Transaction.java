package wallet.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private BigDecimal amount;
}
