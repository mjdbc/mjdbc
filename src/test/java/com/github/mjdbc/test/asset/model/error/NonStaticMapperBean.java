package com.github.mjdbc.test.asset.model.error;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class NonStaticMapperBean {

    @Mapper
    public final DbMapper<NonStaticMapperBean> M = (r) -> new NonStaticMapperBean();

}
