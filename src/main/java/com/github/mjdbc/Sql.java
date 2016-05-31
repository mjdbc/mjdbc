package com.github.mjdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks that the annotated interface method is a SQL query.
 * Raw sql query is provided as value for the annotation.
 * Parameters are mapped by names in @{link DbPreparedStatement} way.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Sql {
    String value() default "";

    int batchChunkSize() default Integer.MAX_VALUE;

    boolean forceUseUpdate() default false;
}
