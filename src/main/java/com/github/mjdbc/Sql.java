package com.github.mjdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for raw sql method with parameters mapped by name.
 * Raw sql query is provided as value for the annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Sql {
    String DEFAULT_VALUE = "";

    String value() default DEFAULT_VALUE;
}
