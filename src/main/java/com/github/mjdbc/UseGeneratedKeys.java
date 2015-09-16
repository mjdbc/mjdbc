package com.github.mjdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Markup for raw sql queries to indicate that result must be returned by statement.getGeneratedKeys() method.
 * The markup is optional for queries started with 'INSERT ' text.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UseGeneratedKeys {
}
