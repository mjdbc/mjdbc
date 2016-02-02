package com.github.mjdbc.test.asset.model.error;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class MultipleMappersBean {

    public final int value;

    public MultipleMappersBean(int value) {
        this.value = value;
    }

    @Mapper
    public static final DbMapper<MultipleMappersBean> SOME_MAPPER1 = (r) -> new MultipleMappersBean(r.getInt(1));

    @Mapper
    public static final DbMapper<MultipleMappersBean> SOME_MAPPER2 = (r) -> new MultipleMappersBean(r.getInt(1));

}
