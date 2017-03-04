package wallet.infra;

import wallet.model.NotEnoughFundsException;
import wallet.model.Transaction;
import wallet.model.Wallet;
import wallet.model.WalletRepo;

import javax.sql.DataSource;
import java.sql.*;

public class WalletRepoImpl implements WalletRepo {

    private final DataSource dataSource;

    public WalletRepoImpl(DataSource dataSource, LiquibaseLoader liquibaseLoader) {
        this.dataSource = dataSource;
    }

    @Override
    public long create() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("insert into wallet(balance)values (0)", Statement.RETURN_GENERATED_KEYS)) {
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    return rs.getLong(1);
                }
            }
        }
    }

    @Override
    public void createTransaction(long walletId, Transaction transaction) throws SQLException {
        if (transaction.getAmount() == null) {
            throw new IllegalArgumentException("missing amount");
        }
        if (transaction.getCategory() == null) {
            throw new IllegalArgumentException("missing category");
        }
        if (transaction.getAmount().stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("invalid amount");
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement stmt = connection.prepareStatement("insert into transaction(wallet_id, category, amount) values (?,?,?)")) {
                stmt.setLong(1, walletId);
                stmt.setString(2, transaction.getCategory().name());
                stmt.setBigDecimal(3, transaction.getAmount());
                if (stmt.executeUpdate() != 1) {
                    throw new IllegalStateException();
                }
            }
            try (PreparedStatement stmt = connection.prepareStatement("update wallet set balance = balance +  ? where id = ?")) {
                stmt.setBigDecimal(1, transaction.getAmount());
                stmt.setLong(2, walletId);
                if (stmt.executeUpdate() != 1) {
                    throw new IllegalStateException();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                if (e.getMessage().contains("chk_balance")) {
                    throw new NotEnoughFundsException();
                }
                throw e;
            }
        }
    }

    @Override
    public Wallet findOne(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("select balance from wallet where id = ?")) {
                stmt.setLong(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    return new Wallet(rs.getBigDecimal("balance"));
                }
            }
        }
    }
}
