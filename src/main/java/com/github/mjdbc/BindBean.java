package com.github.mjdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark parameter as bean in @Sql methods.
 * All properties accessible directly or via get/is methods will be mapped to query values by names.
 * There can be only one @BindBean annotation per methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindBean {
}
