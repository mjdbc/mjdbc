package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.JavaTypesStringSql;
import org.junit.Test;

import java.sql.SQLException;

public class JavaTypesStringTest extends BaseJavaTypesTest<JavaTypesStringSql> {

    public JavaTypesStringTest() {
        super(JavaTypesStringSql.class);
    }

    @Test
    public void checkNullableVarcharStringRead() {
        String v = sql.getNullableVarcharString();
        assertNull(v);
    }

    @Test
    public void checkNullableCharStringRead() {
        String v = sql.getNullableCharString();
        assertNull(v);
    }

    @Test
    public void checkNotNullableVarcharStringRead() {
        String v = sql.getNotNullableVarcharString();
        assertEquals("value", v);
    }

    @Test
    public void checkNotNullableCharStringRead() {
        String v = sql.getNotNullableCharString();
        assertEquals("value     ", v);
    }

    @Test
    public void checkNullableVarcharStringWrite() {
        sql.setNullableVarcharString("x");
        String v = sql.getNullableVarcharString();
        assertEquals("x", v);

        sql.setNullableVarcharString(null);
        v = sql.getNullableVarcharString();
        assertNull(v);
    }

    @Test
    public void checkNullableCharStringWrite() {
        sql.setNullableCharString("x");
        String v = sql.getNullableCharString();
        assertEquals("x         ", v);

        sql.setNullableCharString(null);
        v = sql.getNullableCharString();
        assertNull(v);
    }

    @Test
    public void checkNotNullableVarcharStringWrite() {
        sql.setNotNullableVarcharString("x");
        String v = sql.getNotNullableVarcharString();
        assertEquals("x", v);

        try {
            //noinspection ConstantConditions
            sql.setNotNullableVarcharString(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void checkNotNullableCharStringWrite() {
        sql.setNotNullableCharString("x");
        String v = sql.getNotNullableCharString();
        assertEquals("x         ", v);
        try {
            //noinspection ConstantConditions
            sql.setNotNullableCharString(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void checkVarcharFieldOverflow() {
        try {
            sql.setNullableCharString("12345678901");
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void checkCharFieldOverflow() {
        try {
            sql.setNullableCharString("12345678901");
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }
}
