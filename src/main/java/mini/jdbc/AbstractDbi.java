package mini.jdbc;

public abstract class AbstractDbi implements Dbi {

    protected DbiContext context;

    @Override
    public void setContext(DbiContext context) {
        this.context = context;
    }

    @Override
    public DbiContext getContext() {
        return context;
    }
}
