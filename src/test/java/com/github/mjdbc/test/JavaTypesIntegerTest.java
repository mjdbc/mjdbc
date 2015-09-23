package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.sql.JavaTypesIntegerSql;
import org.junit.Test;

import java.sql.SQLException;

public class JavaTypesIntegerTest extends BaseJavaTypesTest<JavaTypesIntegerSql> {

    public JavaTypesIntegerTest() {
        super(JavaTypesIntegerSql.class);
    }

    @Test
    public void checkNullableIntegerRead() {
        Integer v = sql.getNullableInteger();
        assertNull(v);
    }

    @Test(expected = NullPointerException.class)
    public void checkNullIntegerReadAsIntThrowsException() {
        sql.getNullableIntegerAsInt();
    }

    @Test
    public void checkNullableIntegerWrite() {
        sql.setNullableInteger(10);
        assertEquals(new Integer(10), sql.getNullableInteger());

        sql.setNullableInteger(null);
        assertNull(sql.getNullableInteger());
    }

    @Test
    public void checkNotNullableIntegerRead() {
        Integer v = sql.getNotNullableInteger();
        assertEquals(new Integer(1), v);
    }

    @Test
    public void checkNotNullableIntegerReadAsInt() {
        int v = sql.getNotNullableIntegerAsInt();
        assertEquals(1, v);
    }

    @Test
    public void checkNotNullableIntegerWriteNullThrowsSqlException() {
        try {
            //noinspection ConstantConditions
            sql.setNotNullableInteger(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }
}
