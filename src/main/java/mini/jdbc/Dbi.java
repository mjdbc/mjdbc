package mini.jdbc;

/**
 * Dbi -> Database Interface. Set of methods that incapsulate logic of complex database updates.
 */
public interface Dbi {
    void setContext(DbiContext context);

    DbiContext getContext();
}
