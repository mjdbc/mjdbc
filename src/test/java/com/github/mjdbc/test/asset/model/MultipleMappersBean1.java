package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class MultipleMappersBean1 {

    public final int value;

    public MultipleMappersBean1(int value) {
        this.value = value;
    }

    @Mapper
    public static final DbMapper<MultipleMappersBean1> SOME_MAPPER1 = (r) -> new MultipleMappersBean1(r.getInt(1));

    @Mapper
    public static final DbMapper<MultipleMappersBean1> SOME_MAPPER2 = (r) -> new MultipleMappersBean1(r.getInt(1));

}
