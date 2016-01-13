package com.github.mjdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to customize batch @Sql operations.
 * Effective only if sql statement has a single Iterable/Iterator parameter.
 * Note: by default all statements with Iterable/Iterator parameter are executed in batch
 * mode by 100 per turn.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Batch {
    int value() default 100;
}
