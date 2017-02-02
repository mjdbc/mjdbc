package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.type.impl.DbIntValue;

/**
 * Bean with getters and setters methods and private fields.
 */
public class FinalBean {

    public final long id = 1;

    public final boolean booleanField = false;

    public final int intField = 1;

    public final String stringField = "";

    public final DbIntValue intValueField = new DbIntValue(1);
}
