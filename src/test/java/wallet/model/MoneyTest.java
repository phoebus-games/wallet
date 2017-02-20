package wallet.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class MoneyTest {

    @Test
    public void moneyScaleOk() throws Exception {
        new Money("0.01");
    }

    @Test(expected = IllegalArgumentException.class)
    public void moneyScaleInvalid() throws Exception {
        new Money("0.001");
    }

    @Test
    public void checkGetter() throws Exception {
        assertEquals(BigDecimal.ZERO, new Money(BigDecimal.ZERO).getAmount());
    }

    @Test
    public void checkToString() throws Exception {
        assertEquals("0.00", new Money(0).toString());
    }
}