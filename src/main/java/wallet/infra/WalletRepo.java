package wallet.infra;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wallet.model.Wallet;

import java.math.BigDecimal;

import static java.util.Collections.singletonMap;

@Repository
public class WalletRepo {
    private final JdbcTemplate template;

    public WalletRepo(JdbcTemplate template) {
        this.template = template;
    }

    public long create() {
        return new SimpleJdbcInsert(template)
                .withTableName("wallet")
                .usingGeneratedKeyColumns("id")
                .usingColumns("balance")
                .executeAndReturnKey(singletonMap("balance", BigDecimal.ZERO))
                .longValue();
    }

    public void save(long id, Wallet wallet) {
        int numUpdated = template.update("update wallet set balance = ? where id = ?", wallet.getBalance(), id);
        if (numUpdated != 1) {
            throw new IllegalStateException();
        }
    }

    public Wallet findOne(long id) {
        return template.queryForObject("select balance from wallet where id = ?", new Object[]{id}, (rs, rowNum) -> {
            Wallet wallet = new Wallet();
            wallet.setBalance(rs.getBigDecimal("balance"));
            return wallet;
        });
    }
}
