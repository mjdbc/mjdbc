package com.github.mjdbc;

import com.github.mjdbc.batch.BatchHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class SqlProxy implements InvocationHandler {
    @NotNull
    private final DbImpl db;

    @NotNull
    private String interfaceSimpleName;

    SqlProxy(@NotNull DbImpl db, @NotNull String interfaceSimpleName) {
        this.db = db;
        this.interfaceSimpleName = interfaceSimpleName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DbImpl.OpRef p = db.opByMethod.get(method);
        if (p == null) { // java.lang.Object methods
            if (method.getName().equals("toString")) {
                return interfaceSimpleName + "$Proxy";
            }
            return method.invoke(this, args);
        }
        long t0 = System.nanoTime();
        try {
            return db.execute(c -> {
                //noinspection unchecked
                DbStatement s = new DbStatement(c, p.parsedSql, p.resultMapper, p.parametersNamesMapping, p.useGeneratedKeys);
                if (args != null) {
                    if (p.batchHandlerFactory != null) {
                        return executeBatch(p, s, args);
                    }
                    bindSingleStatementArgs(p, s, args, null);
                }
                if (p.resultMapper != Mappers.VoidMapper) {
                    if (p.useGeneratedKeys) {
                        return s.updateAndGetGeneratedKeys();
                    } else {
                        Object res = s.query();
                        return res == null && p.resultMapper.getClass() == ListMapper.class ? new ArrayList<>() : res;
                    }
                }
                s.update();
                return null;
            });
        } finally {
            db.updateTimer(method, t0);
        }
    }

    private Object executeBatch(@NotNull DbImpl.OpRef p, @NotNull DbStatement s, @NotNull Object[] args) throws IllegalAccessException, InvocationTargetException, java.sql.SQLException {
        int batchSize = 100;
        assert p.batchHandlerFactory != null;
        //noinspection unchecked
        BatchHandler bh = p.batchHandlerFactory.createHandler(args[p.batchParameterIdx]);
        boolean hasNext = bh.hasNext();
        for (int i = 0; hasNext; i++) {
            bindSingleStatementArgs(p, s, args, bh);
            s.statement.addBatch();
            hasNext = bh.hasNext();
            if (i % batchSize == batchSize - 1 || !hasNext) {
                s.statement.executeBatch(); //todo: support executeLargeBatch
            }
        }
        return null; //todo: return valid batch results
    }

    private void bindSingleStatementArgs(@NotNull DbImpl.OpRef p, @NotNull DbStatement s, @NotNull Object[] args, @Nullable BatchHandler batchHandler) throws IllegalAccessException, InvocationTargetException, java.sql.SQLException {
        for (DbImpl.BindInfo bi : p.bindings) {
            List<Integer> sqlIndexes = p.parametersNamesMapping.get(bi.mappedName);
            if (sqlIndexes == null) {
                continue;
            }
            if (bi.batchParameter) {
                assert batchHandler != null;
                Object value = batchHandler.next();
                for (Integer idx : sqlIndexes) {
                    //noinspection unchecked
                    bi.binder.bind(s.statement, idx, value);
                }
            } else {
                for (Integer idx : sqlIndexes) {
                    Object arg = args[bi.argumentIdx];
                    Object value = bi.field != null ? bi.field.get(arg) : bi.getter != null ? bi.getter.invoke(arg) : arg;
                    //noinspection unchecked
                    bi.binder.bind(s.statement, idx, value);
                }
            }
        }
    }
}
