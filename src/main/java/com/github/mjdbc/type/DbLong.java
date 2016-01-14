package com.github.mjdbc.type;

/**
 * Objects of a class that implements this interface will be saved in database as long integers.
 */
public interface DbLong {
    /**
     * @return database object representation.
     */
    long getDbValue();
}
