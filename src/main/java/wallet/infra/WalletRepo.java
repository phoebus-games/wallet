package wallet.infra;

import wallet.model.Wallet;

import javax.sql.DataSource;
import java.sql.*;

public class WalletRepo {

    private final DataSource dataSource;

    public WalletRepo(DataSource dataSource, LiquibaseLoader liquibaseLoader) {
        this.dataSource = dataSource;
    }

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

    public void save(long id, Wallet wallet) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement("update wallet set balance = balance + ? where id = ?")) {
                stmt.setBigDecimal(1, wallet.getBalance());
                stmt.setLong(2, id);
                if (stmt.executeUpdate() != 1) {
                    throw new IllegalStateException();
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("chk_balance")) {
                throw new NotEnoughFundsException();
            }
            throw e;
        }
    }

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
