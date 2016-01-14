package com.github.mjdbc.type;

/**
 * Objects of a class that implements this interface will be saved in database as integers.
 */
public interface DbInt {
    /**
     * @return database object representation.
     */
    int getDbValue();
}
