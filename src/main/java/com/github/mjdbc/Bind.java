package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to bind parameters to names in @Sql methods.
 * The value corresponds to parameter value prefixed with ':' colon.
 * Example: SELECT * FROM users WHERE id=:someId.
 * In the example above the 'someId' is @Bind annotation value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Bind {
    @NotNull
    String value();
}
