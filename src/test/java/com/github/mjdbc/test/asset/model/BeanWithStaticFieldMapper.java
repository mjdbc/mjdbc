package com.github.mjdbc.test.asset.model;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

public class BeanWithStaticFieldMapper {

    @Mapper
    public static final DbMapper<BeanWithStaticFieldMapper> MAPPER_v1_1 = r -> null;

}
