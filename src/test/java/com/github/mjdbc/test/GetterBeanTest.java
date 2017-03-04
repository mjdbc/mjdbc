package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.model.GetterBean;
import com.github.mjdbc.test.asset.sql.GetterBeanSql;
import com.github.mjdbc.test.asset.types.DbIntValue;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class GetterBeanTest extends BaseSqlTest<GetterBeanSql> {

    public GetterBeanTest() {
        super(GetterBeanSql.class, "beans");
    }

    @Test
    public void checkInsertWithGetter() {
        GetterBean original = createBean();

        long id = sql.insert(original);
        assertTrue(id > 0);

        GetterBean fromDb = sql.get(id);

        assertEquals(original.isBooleanField(), fromDb.isBooleanField());
        assertEquals(original.getIntField(), fromDb.getIntField());
        assertEquals(original.getStringField(), fromDb.getStringField());
        assertEquals(original.getIntValueField(), fromDb.getIntValueField());
    }

    @NotNull
    private GetterBean createBean() {
        GetterBean original = new GetterBean();
        original.setBooleanField(true);
        original.setIntField(10);
        original.setStringField("20");
        original.setIntValueField(new DbIntValue(30));
        return original;
    }

    @Test
    public void checkUpdateWithGetter() {
        GetterBean original = createBean();

        long id = sql.insert(original);
        assertTrue(id > 0);

        original.setId(id);
        original.setBooleanField(false);
        original.setIntField(40);
        original.setStringField("50");
        original.setIntValueField(new DbIntValue(60));
        sql.update(original);

        GetterBean fromDb = sql.get(id);

        assertEquals(original.isBooleanField(), fromDb.isBooleanField());
        assertEquals(original.getIntField(), fromDb.getIntField());
        assertEquals(original.getStringField(), fromDb.getStringField());
        assertEquals(original.getIntValueField(), fromDb.getIntValueField());
    }
}
