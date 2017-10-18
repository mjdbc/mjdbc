package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.sql.DbValueSql;
import com.github.mjdbc.test.asset.types.DbIntValue;
import com.github.mjdbc.test.asset.types.DbLongValue;
import java.sql.Timestamp;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for DbValue (Int,Long..) support.
 */
public class DbValuesSqlTest extends DbTest {
    private DbValueSql sql;

    public DbValuesSqlTest() {
        super("types");
    }

    @Before
    public void setUp() {
        super.setUp();
        sql = db.attachSql(DbValueSql.class);
    }

    @Test
    public void checkDbIntValueSql() {
        sql.setNullableDbInt(null);
        Integer v1 = sql.getNullableInt();
        assertNull(v1);

        sql.setNullableDbInt(new DbIntValue(1));
        Integer v2 = sql.getNullableInt();
        assertEquals(new Integer(1), v2);
    }


    @Test
    public void checkDbLongValueSql() {
        sql.setNullableDbLong(null);
        Long v1 = sql.getNullableLong();
        assertNull(v1);

        sql.setNullableDbLong(new DbLongValue(1));
        Long v2 = sql.getNullableLong();
        assertEquals(new Long(1), v2);
    }


    @Test
    public void checkDbStringValueSql() {
        sql.setNullableDbString(null);
        String v1 = sql.getNullableString();
        assertNull(v1);

        sql.setNullableDbString(() -> "1");
        String v2 = sql.getNullableString();
        assertEquals("1", v2);
    }


    @Test
    public void checkDbTimestampValueSql() {
        sql.setNullableDbTimestamp(null);
        Timestamp v1 = sql.getNullableTimestamp();
        assertNull(v1);

        sql.setNullableDbTimestamp(() -> new Timestamp(1));
        Timestamp v2 = sql.getNullableTimestamp();
        assertEquals(new Timestamp(1), v2);
    }

}
