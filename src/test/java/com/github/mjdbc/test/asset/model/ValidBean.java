package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class ValidBean {

    public final int value;

    public Object someTransientFieldWithNoBinder = new Object();

    public ValidBean(int value) {
        this.value = value;
    }

    @Mapper
    public static final DbMapper<ValidBean> SOME_MAPPER = (r) -> new ValidBean(r.getInt(1));
}
