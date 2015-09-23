package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.sql.JavaTypesOthersSql;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class JavaTypesOthersTest extends BaseJavaTypesTest<JavaTypesOthersSql> {

    public JavaTypesOthersTest() {
        super(JavaTypesOthersSql.class);
    }

    @Test
    public void checkBigDecimal() {
        // check original value
        BigDecimal v0 = sql.getNullableBigDecimal();
        assertEquals(new BigDecimal(1), v0);

        //check null
        sql.setNullableBigDecimal(null);
        BigDecimal v1 = sql.getNullableBigDecimal();
        assertNull(v1);

        BigDecimal newValue = new BigDecimal(2);
        sql.setNullableBigDecimal(newValue);
        BigDecimal v3 = sql.getNullableBigDecimal();
        assertEquals(newValue, v3);
    }

    @Test
    public void checkBoolean() {
        // check original value
        Boolean v0 = sql.getNullableBoolean();
        assertTrue(v0);
        boolean v0b = sql.getNullableBooleanAsBool();
        assertTrue(v0b);

        // check null
        sql.setNullableBoolean(null);
        Boolean v1 = sql.getNullableBoolean();
        assertNull(v1);
        try {
            sql.getNullableBooleanAsBool();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }

        // check Object setter
        sql.setNullableBooleanAsBool(Boolean.FALSE);
        Boolean v2 = sql.getNullableBoolean();
        assertFalse(v2);
        boolean v2b = sql.getNullableBooleanAsBool();
        assertFalse(v2b);

        // check primitive setter
        sql.setNullableBooleanAsBool(Boolean.TRUE);
        Boolean v3 = sql.getNullableBoolean();
        assertTrue(v3);
        boolean v3b = sql.getNullableBooleanAsBool();
        assertTrue(v3b);
    }

    @Test
    public void checkByte() {
        // check original value
        Byte v0 = sql.getNullableByte();
        assertEquals(new Byte((byte) 1), v0);
        byte v0b = sql.getNullableByteAsByte();
        assertTrue(v0b == 1);

        // check null
        sql.setNullableByte(null);
        Byte v1 = sql.getNullableByte();
        assertNull(v1);
        try {
            sql.getNullableByteAsByte();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }

        // check Object setter
        sql.setNullableByte(Byte.MIN_VALUE);
        Byte v2 = sql.getNullableByte();
        assertEquals((Byte) Byte.MIN_VALUE, v2);
        byte v2b = sql.getNullableByteAsByte();
        assertTrue(v2b == Byte.MIN_VALUE);

        // check primitive setter
        sql.setNullableByteAsByte(Byte.MAX_VALUE);
        Byte v3 = sql.getNullableByte();
        assertEquals((Byte) Byte.MAX_VALUE, v3);
        byte v3b = sql.getNullableByteAsByte();
        assertTrue(v3b == Byte.MAX_VALUE);
    }

    @Test
    public void checkLong() {
        // check original value
        Long v0 = sql.getNullableLong();
        assertEquals(new Long(1L), v0);
        long v0b = sql.getNullableLongAsLong();
        assertTrue(v0b == 1L);

        // check null
        sql.setNullableLong(null);
        Long v1 = sql.getNullableLong();
        assertNull(v1);
        try {
            sql.getNullableLongAsLong();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }

        // check Object setter
        sql.setNullableLong(Long.MIN_VALUE);
        Long v2 = sql.getNullableLong();
        assertEquals((Long) Long.MIN_VALUE, v2);
        long v2b = sql.getNullableLongAsLong();
        assertTrue(v2b == Long.MIN_VALUE);

        // check primitive setter
        sql.setNullableLongAsLong(Long.MAX_VALUE);
        Long v3 = sql.getNullableLong();
        assertEquals((Long) Long.MAX_VALUE, v3);
        long v3b = sql.getNullableLongAsLong();
        assertTrue(v3b == Long.MAX_VALUE);
    }

    @Test
    public void checkShort() {
        // check original value
        Short v0 = sql.getNullableShort();
        assertEquals(new Short((short) 1), v0);
        short v0b = sql.getNullableShortAsShort();
        assertTrue(v0b == 1);

        // check null
        sql.setNullableShort(null);
        Short v1 = sql.getNullableShort();
        assertNull(v1);
        try {
            sql.getNullableShortAsShort();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }

        // check Object setter
        sql.setNullableShort(Short.MIN_VALUE);
        Short v2 = sql.getNullableShort();
        assertEquals((Short) Short.MIN_VALUE, v2);
        short v2b = sql.getNullableShortAsShort();
        assertTrue(v2b == Short.MIN_VALUE);

        // check primitive setter
        sql.setNullableShortAsShort(Short.MAX_VALUE);
        Short v3 = sql.getNullableShort();
        assertEquals((Short) Short.MAX_VALUE, v3);
        short v3b = sql.getNullableShortAsShort();
        assertTrue(v3b == Short.MAX_VALUE);
    }

    @Test
    public void checkCharacter() {
        // check original value
        Character v0 = sql.getNullableCharacter();
        assertEquals((Character) (char) 1, v0);
        char v0b = sql.getNullableCharacterAsChar();
        assertTrue(v0b == 1);

        // check null
        sql.setNullableCharacter(null);
        Character v1 = sql.getNullableCharacter();
        assertNull(v1);
        try {
            sql.getNullableCharacterAsChar();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }

        // check Object setter
        sql.setNullableCharacter(Character.MIN_VALUE);
        Character v2 = sql.getNullableCharacter();
        assertEquals((Character) Character.MIN_VALUE, v2);
        char v2b = sql.getNullableCharacterAsChar();
        assertTrue(v2b == Character.MIN_VALUE);

        // check primitive setter
        sql.setNullableCharacterAsChar(Character.MAX_VALUE);
        Character v3 = sql.getNullableCharacter();
        assertEquals((Character) Character.MAX_VALUE, v3);
        char v3b = sql.getNullableCharacterAsChar();
        assertTrue(v3b == Character.MAX_VALUE);
    }

    @Test
    public void checkDouble() {
        // check original value
        Double v0 = sql.getNullableDouble();
        assertEquals((Double) (double) 1D, v0);
        double v0b = sql.getNullableDoubleAsDouble();
        assertTrue(v0b == 1D);

        // check null
        sql.setNullableDouble(null);
        Double v1 = sql.getNullableDouble();
        assertNull(v1);
        try {
            sql.getNullableDoubleAsDouble();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }

        // check Object setter
        sql.setNullableDouble(Double.MIN_VALUE);
        Double v2 = sql.getNullableDouble();
        assertEquals((Double) Double.MIN_VALUE, v2);
        double v2b = sql.getNullableDoubleAsDouble();
        assertTrue(v2b == Double.MIN_VALUE);

        // check primitive setter
        sql.setNullableDoubleAsDouble(Double.MAX_VALUE);
        Double v3 = sql.getNullableDouble();
        assertEquals((Double) Double.MAX_VALUE, v3);
        double v3b = sql.getNullableDoubleAsDouble();
        assertTrue(v3b == Double.MAX_VALUE);


// NANs, Positive & Negative infinity do not work with Derby:
// https://issues.apache.org/jira/browse/DERBY-3290

        // check NAN
//        sql.setNullableDoubleAsDouble(Double.NaN);
//        Double v4 = sql.getNullableDouble();
//        assertTrue(v4.isNaN());
//        double v4b = sql.getNullableDoubleAsDouble();
//        assertTrue(Double.isNaN(v4b));

        // check positive infinity
//        sql.setNullableDoubleAsDouble(Double.POSITIVE_INFINITY);
//        Double v5 = sql.getNullableDouble();
//        assertEquals((Double) Double.POSITIVE_INFINITY, v5);
//        double v5b = sql.getNullableDoubleAsDouble();
//        assertTrue(Double.POSITIVE_INFINITY == v5b);

        // check negative infinity
//        sql.setNullableDoubleAsDouble(Double.NEGATIVE_INFINITY);
//        Double v6 = sql.getNullableDouble();
//        assertEquals((Double) Double.NEGATIVE_INFINITY, v6);
//        double v6b = sql.getNullableDoubleAsDouble();
//        assertTrue(Double.NEGATIVE_INFINITY == v6b);
    }

    @Test
    public void checkFloat() {
        // check original value
        Float v0 = sql.getNullableFloat();
        assertEquals((Float) 1F, v0);
        float v0b = sql.getNullableFloatAsFloat();
        assertTrue(v0b == 1F);

        // check null
        sql.setNullableFloat(null);
        Float v1 = sql.getNullableFloat();
        assertNull(v1);
        try {
            sql.getNullableFloatAsFloat();
            fail();
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
        }

        // check Object setter
        sql.setNullableFloat(Float.MIN_VALUE);
        Float v2 = sql.getNullableFloat();
        assertEquals((Float) Float.MIN_VALUE, v2);
        float v2b = sql.getNullableFloatAsFloat();
        assertTrue(v2b == Float.MIN_VALUE);

        // check primitive setter
        sql.setNullableFloatAsFloat(Float.MAX_VALUE);
        Float v3 = sql.getNullableFloat();
        assertEquals((Float) Float.MAX_VALUE, v3);
        float v3b = sql.getNullableFloatAsFloat();
        assertTrue(v3b == Float.MAX_VALUE);


// NANs, Positive & Negative infinity do not work with Derby:
// https://issues.apache.org/jira/browse/DERBY-3290

        // check NAN
//        sql.setNullableFloatAsFloat(Float.NaN);
//        Float v4 = sql.getNullableFloat();
//        assertTrue(v4.isNaN());
//        float v4b = sql.getNullableFloatAsFloat();
//        assertTrue(Float.isNaN(v4b));

        // check positive infinity
//        sql.setNullableFloatAsFloat(Float.POSITIVE_INFINITY);
//        Float v5 = sql.getNullableFloat();
//        assertEquals((Float) Float.POSITIVE_INFINITY, v5);
//        float v5b = sql.getNullableFloatAsFloat();
//        assertTrue(Float.POSITIVE_INFINITY == v5b);

        // check negative infinity
//        sql.setNullableFloatAsFloat(Float.NEGATIVE_INFINITY);
//        Float v6 = sql.getNullableFloat();
//        assertEquals((Float) Float.NEGATIVE_INFINITY, v6);
//        float v6b = sql.getNullableFloatAsFloat();
//        assertTrue(Float.NEGATIVE_INFINITY == v6b);
    }


    @Test
    public void checkJavaUtilDate() {
        // check original value
        java.util.Date v0 = sql.getNullableDate();
        assertNotNull(v0);
        assertEquals(java.sql.Date.class, v0.getClass());

        // check nulls
        sql.setNullableDate(null);
        java.util.Date v1 = sql.getNullableDate();
        assertNull(v1);

        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(0);
        c.set(Calendar.YEAR, 2015);

        // check java.util.Date <-> java.sql.Date compatibility.
        java.util.Date utilDate = new java.util.Date(c.getTimeInMillis());
        sql.setNullableDate(utilDate);
        java.util.Date v3 = sql.getNullableDate();
        assertNotNull(v3);
        assertEquals(java.sql.Date.class, v3.getClass());
        assertEquals(utilDate.getTime(), v3.getTime());

        // check java.util.Date <-> java.sql.Date compatibility.
        java.sql.Date sqlDate = new java.sql.Date(c.getTimeInMillis());
        sql.setNullableDate(sqlDate);
        java.util.Date v4 = sql.getNullableDate();
        assertNotNull(v4);
        assertEquals(java.sql.Date.class, v4.getClass());
        assertEquals(sqlDate.getTime(), v4.getTime());
    }

    @Test
    public void checkJavaSqlDate() {
        // check original value
        java.sql.Date v0 = sql.getNullableSqlDate();
        assertNotNull(v0);

        // check nulls
        sql.setNullableSqlDate(null);
        java.sql.Date v1 = sql.getNullableSqlDate();
        assertNull(v1);

        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(0);
        c.set(Calendar.YEAR, 2015);

        java.sql.Date sqlDate = new java.sql.Date(c.getTimeInMillis());
        sql.setNullableSqlDate(sqlDate);
        java.sql.Date v3 = sql.getNullableSqlDate();
        assertNotNull(v3);
        assertEquals(sqlDate.getTime(), v3.getTime());
    }


    @Test
    public void checkTime() {
        // check original value
        Time v0 = sql.getNullableTime();
        assertNotNull(v0);

        // check nulls
        sql.setNullableTime(null);
        Time v1 = sql.getNullableTime();
        assertNull(v1);

        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        c.setTimeInMillis(0);
        c.set(Calendar.HOUR_OF_DAY, 16);
        c.set(Calendar.MINUTE, 15);
        c.set(Calendar.SECOND, 22);

        Time sqlTime = new java.sql.Time(c.getTimeInMillis());
        sql.setNullableTime(sqlTime);
        Time v3 = sql.getNullableTime();
        assertNotNull(v3);
        assertEquals(sqlTime.getTime(), v3.getTime());
    }

    @Test
    public void checkTimestamp() {
        // check original value
        Timestamp v0 = sql.getNullableTimestamp();
        assertNotNull(v0);

        // check nulls
        sql.setNullableTimestamp(null);
        Timestamp v1 = sql.getNullableTimestamp();
        assertNull(v1);

        sql.setNullableTimestamp(v0);
        Timestamp v2 = sql.getNullableTimestamp();
        assertEquals(v0, v2);
    }
}
