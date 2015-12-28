package com.github.mjdbc.test.util;

import org.jetbrains.annotations.NotNull;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Helper class to track number of getConnection calls;
 */
public class ProfiledDataSource implements javax.sql.DataSource {
    @NotNull
    private DataSource ds;

    public int nGetConnectionCalls;

    public ProfiledDataSource(@NotNull DataSource ds) {
        this.ds = ds;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return ds.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        ds.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        ds.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return ds.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return ds.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> c) throws SQLException {
        return ds.unwrap(c);
    }

    @Override
    public boolean isWrapperFor(Class<?> c) throws SQLException {
        return ds.isWrapperFor(c);
    }

    @Override
    public Connection getConnection() throws SQLException {
        nGetConnectionCalls++;
        return new ProfiledConnection(ds.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        nGetConnectionCalls++;
        return new ProfiledConnection(ds.getConnection(username, password));
    }
}
