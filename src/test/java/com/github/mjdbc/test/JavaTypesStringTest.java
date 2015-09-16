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
        String v = typesSql.getNullableVarcharString();
        assertNull(v);
    }

    @Test
    public void checkNullableCharStringRead() {
        String v = typesSql.getNullableCharString();
        assertNull(v);
    }

    @Test
    public void checkNotNullableVarcharStringRead() {
        String v = typesSql.getNotNullableVarcharString();
        assertEquals("value", v);
    }

    @Test
    public void checkNotNullableCharStringRead() {
        String v = typesSql.getNotNullableCharString();
        assertEquals("value     ", v);
    }

    @Test
    public void checkNullableVarcharStringWrite() {
        typesSql.setNullableVarcharString("x");
        String v = typesSql.getNullableVarcharString();
        assertEquals("x", v);

        typesSql.setNullableVarcharString(null);
        v = typesSql.getNullableVarcharString();
        assertNull(v);
    }

    @Test
    public void checkNullableCharStringWrite() {
        typesSql.setNullableCharString("x");
        String v = typesSql.getNullableCharString();
        assertEquals("x         ", v);

        typesSql.setNullableCharString(null);
        v = typesSql.getNullableCharString();
        assertNull(v);
    }

    @Test
    public void checkNotNullableVarcharStringWrite() {
        typesSql.setNotNullableVarcharString("x");
        String v = typesSql.getNotNullableVarcharString();
        assertEquals("x", v);

        try {
            //noinspection ConstantConditions
            typesSql.setNotNullableVarcharString(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void checkNotNullableCharStringWrite() {
        typesSql.setNotNullableCharString("x");
        String v = typesSql.getNotNullableCharString();
        assertEquals("x         ", v);
        try {
            //noinspection ConstantConditions
            typesSql.setNotNullableCharString(null);
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void checkVarcharFieldOverflow() {
        try {
            typesSql.setNullableCharString("12345678901");
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }

    @Test
    public void checkCharFieldOverflow() {
        try {
            typesSql.setNullableCharString("12345678901");
            fail();
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof SQLException);
        }
    }
}
