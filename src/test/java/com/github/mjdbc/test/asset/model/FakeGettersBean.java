package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;
import com.github.mjdbc.type.impl.DbIntValue;

/**
 * Bean with fake getters -> getters with parameters are not normal getters
 */
public class FakeGettersBean {

    private long id;

    private boolean booleanField;

    private int intField;

    private String stringField;

    private DbIntValue intValueField;


    public long getId(int x) {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBooleanField(int x) {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public int getIntField(int x) {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public String getStringField(int x) {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public DbIntValue getIntValueField(int x) {
        return intValueField;
    }

    public void setIntValueField(DbIntValue intValueField) {
        this.intValueField = intValueField;
    }

    /**
     * Class to create User object from result set.
     */
    @Mapper
    public static final DbMapper<FakeGettersBean> MAPPER = (r) -> {
        FakeGettersBean res = new FakeGettersBean();
        res.id = r.getInt("id");
        res.booleanField = r.getBoolean("boolean_field");
        res.intField = r.getInt("int_field");
        res.stringField = r.getString("string_field");
        res.intValueField = new DbIntValue(r.getInt("int_value_field"));
        return res;
    };
}
