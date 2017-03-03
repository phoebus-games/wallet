package wallet.model;

import java.sql.SQLException;

public interface WalletRepo {
    long create() throws SQLException;

    void save(long id, Wallet wallet) throws SQLException;

    Wallet findOne(long id) throws SQLException;
}
