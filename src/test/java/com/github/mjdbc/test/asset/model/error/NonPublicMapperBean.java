package com.github.mjdbc.test.asset.model.error;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class NonPublicMapperBean {

    @Mapper
    final static DbMapper<NonPublicMapperBean> M = (r) -> new NonPublicMapperBean();

}
