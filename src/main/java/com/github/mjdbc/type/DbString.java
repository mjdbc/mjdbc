package com.github.mjdbc.type;

/**
 * Objects of a class that implements this interface will be saved in database as strings.
 */
public interface DbString {
    /**
     * @return database object representation.
     */
    String getDbValue();
}
