package mini.jdbc;

/**
 *
 */
public interface Dbi {
    void setContext(DbContext context);

    DbContext getContext();
}
