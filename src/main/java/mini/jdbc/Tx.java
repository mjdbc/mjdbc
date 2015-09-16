package mini.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker that method must be executed as transaction.
 * If execution flow is already inside transaction no new transaction is created
 * and the code inside of the method becomes a part of parent transaction.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Tx {
}
