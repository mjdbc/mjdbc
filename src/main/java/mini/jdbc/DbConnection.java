package mini.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DbConnection {
    public final Connection sqlConnection;

    @NotNull
    protected List<DbStatement> statementsToClose = new ArrayList<>();

    public DbConnection(@NotNull Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    public void commit() throws SQLException {
        sqlConnection.commit();
    }

    public void rollback() throws SQLException {
        sqlConnection.rollback();
    }

    public void close() throws SQLException {
        for (DbStatement s : statementsToClose) {
            s.close();
        }
        sqlConnection.close();
    }
}
