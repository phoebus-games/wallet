package wallet.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class WalletTest {

    @Test(expected = IllegalArgumentException.class)
    public void cannotBeNullBalance() throws Exception {
        new Wallet(null);
    }

    @Test
    public void setBalance() throws Exception {
        Wallet wallet = new Wallet(new BigDecimal(1000));
        wallet.setBalance(new BigDecimal(10));
        assertEquals(new BigDecimal(10), wallet.getBalance());
    }

    @Test
    public void createTransaction() throws Exception {
        Wallet wallet = new Wallet(new BigDecimal(1000));
        wallet.createTransaction(new BigDecimal(10));
        assertEquals(new BigDecimal(990), wallet.getBalance());
    }
}