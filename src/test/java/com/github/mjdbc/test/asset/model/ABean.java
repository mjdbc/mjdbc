package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class ABean {

    public final int value;

    public ABean(int value) {
    this.value = value;
    }

    @Mapper
    public static final DbMapper<ABean> SOME_MAPPER = (r) -> new ABean(r.getInt(1));


}
