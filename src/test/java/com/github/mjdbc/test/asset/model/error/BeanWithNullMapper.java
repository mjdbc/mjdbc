package com.github.mjdbc.test.asset.model.error;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class BeanWithNullMapper {

    public final int value;

    public BeanWithNullMapper(int value) {
        this.value = value;
    }

    @Mapper
    public static final DbMapper<BeanWithNullMapper> MAPPER = null;

}
