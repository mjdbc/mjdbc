package mini.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to bind parameters to names in @Sql methods.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Bind {
    String DEFAULT_VALUE = "";

    String value() default DEFAULT_VALUE;
}
