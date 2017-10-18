package com.github.mjdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * Maps list of result set arguments.
 */
final class ListMapper implements DbMapper<List> {

    @NotNull
    private final DbMapper elementMapper;

    ListMapper(@NotNull DbMapper elementMapper) {
        this.elementMapper = elementMapper;
    }

    @NotNull
    @Override
    public List map(@NotNull ResultSet r) throws SQLException {
        ArrayList res = new ArrayList();
        do {
            //noinspection unchecked
            res.add(elementMapper.map(r));
        } while (r.next());
        return res;
    }
}
