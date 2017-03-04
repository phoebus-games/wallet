package wallet.model;

import java.sql.SQLException;

public interface WalletRepo {
    long create() throws SQLException;

    void createTransaction(long walletId, Transaction transaction) throws SQLException;

    Wallet findOne(long id) throws SQLException;
}
