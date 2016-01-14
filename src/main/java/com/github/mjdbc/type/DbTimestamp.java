package com.github.mjdbc.type;


import java.sql.Timestamp;

/**
 * Objects of a class that implements this interface will be saved in database as SQL timestamps.
 */
public interface DbTimestamp {
    /**
     * @return database object representation.
     */
    Timestamp getDbValue();
}
