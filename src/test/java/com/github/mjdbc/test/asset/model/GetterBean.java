package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;
import com.github.mjdbc.test.asset.types.DbIntValue;

/**
 * Bean with getters and setters methods and private fields.
 */
public class GetterBean {

    private long id;

    private boolean booleanField;

    private int intField;

    private String stringField;

    private DbIntValue intValueField;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public DbIntValue getIntValueField() {
        return intValueField;
    }

    public void setIntValueField(DbIntValue intValueField) {
        this.intValueField = intValueField;
    }

    /**
     * Class to create User object from result set.
     */
    @Mapper
    public static final DbMapper<GetterBean> MAPPER = (r) -> {
        GetterBean res = new GetterBean();
        res.id = r.getInt("id");
        res.booleanField = r.getBoolean("boolean_field");
        res.intField = r.getInt("int_field");
        res.stringField = r.getString("string_field");
        res.intValueField = new DbIntValue(r.getInt("int_value_field"));
        return res;
    };
}
