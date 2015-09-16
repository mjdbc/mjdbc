package com.github.mjdbc.mapper;

import com.github.mjdbc.DbMapper;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class VoidMapper implements DbMapper<Void> {
    public static final VoidMapper INSTANCE = new VoidMapper();

    @NotNull
    @Override
    public Void map(ResultSet r) throws SQLException {
        return null;
    }
}
