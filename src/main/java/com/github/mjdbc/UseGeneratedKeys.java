package com.github.mjdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Markup for {@link Sql} queries that indicates that result must be returned by statement.getGeneratedKeys() method.
 * The markup is optional for queries started with 'INSERT ' keyword.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UseGeneratedKeys {
}
