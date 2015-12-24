package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class MultipleMappersBean2 {

    public final int value;

    public MultipleMappersBean2(int value) {
        this.value = value;
    }

    @Mapper
    public static final DbMapper<MultipleMappersBean2> SOME_MAPPER1 = (r) -> new MultipleMappersBean2(r.getInt(1));

    @SuppressWarnings("unused")
    public static final DbMapper<MultipleMappersBean2> MAPPER = (r) -> new MultipleMappersBean2(r.getInt(1));


}
