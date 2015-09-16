package com.github.mjdbc.test;

import com.github.mjdbc.DbImpl;
import com.github.mjdbc.test.asset.JavaTypesSql;
import com.github.mjdbc.test.util.DbUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class JavaTypesTest extends Assert {
    /**
     * Low level connection pool.
     */
    private HikariDataSource ds;


    /**
     * Set of raw SQL queries.
     */
    private JavaTypesSql typesSql;

    @Before
    public void setUp() {
        ds = DbUtils.prepareDataSource("types");
        DbImpl db = new DbImpl(ds);
        typesSql = db.attachSql(JavaTypesSql.class);
    }

    @After
    public void tearDown() {
        ds.close();
    }

    @Test
    public void checkNullableIntegerRead() {
        Integer v = typesSql.getNullableInteger();
        assertNull(v);
    }

    @Test(expected = NullPointerException.class)
    public void checkNullIntegerReadAsIntThrowsException() {
        typesSql.getNullableIntegerAsInt();
    }

    @Test
    public void checkNullableIntegerWrite() {
        typesSql.setNullableInteger(10);
        assertEquals(new Integer(10), typesSql.getNullableInteger());

        typesSql.setNullableInteger(null);
        assertNull(typesSql.getNullableInteger());
    }

    @Test
    public void checkNotNullableIntegerRead() {
        Integer v = typesSql.getNotNullableInteger();
        assertEquals(new Integer(1), v);
    }

    @Test
    public void checkNotNullableIntegerReadAsInt() {
        int v = typesSql.getNotNullableIntegerAsInt();
        assertEquals(1, v);
    }

    @Test
    public void checkNotNullableIntegerWriteNullThrowsSqlException() {
        try {
            //noinspection ConstantConditions
            typesSql.setNotNullableInteger(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }

}
