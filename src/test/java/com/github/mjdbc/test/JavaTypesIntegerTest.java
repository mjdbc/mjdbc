package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.JavaTypesIntegerSql;
import org.junit.Test;

import java.sql.SQLException;

public class JavaTypesIntegerTest extends BaseJavaTypesTest<JavaTypesIntegerSql> {

    public JavaTypesIntegerTest() {
        super(JavaTypesIntegerSql.class);
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
