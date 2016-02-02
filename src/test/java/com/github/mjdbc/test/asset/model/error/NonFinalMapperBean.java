package com.github.mjdbc.test.asset.model.error;

import com.github.mjdbc.DbMapper;
import com.github.mjdbc.Mapper;

/**
 * Object mapped by field marked with annotation.
 */
public class NonFinalMapperBean {

    @Mapper
    public static DbMapper<NonFinalMapperBean> M = (r) -> new NonFinalMapperBean();

}
