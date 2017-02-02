package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.model.FinalBean;
import com.github.mjdbc.test.asset.sql.FinalBeanSql;
import org.junit.Test;

public class FinalBeanTest extends BaseSqlTest<FinalBeanSql> {

    public FinalBeanTest() {
        super(FinalBeanSql.class, "beans");
    }

    @Test
    public void checkInsert() {
        long id = sql.insert(new FinalBean());
        assertTrue(id > 0);
    }

    @Test
    public void checkUpdate() {
        sql.update(new FinalBean());
    }

}
