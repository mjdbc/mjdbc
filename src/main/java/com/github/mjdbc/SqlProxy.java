package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationHandler;
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

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        DbImpl.OpRef p = db.opByMethod.get(method);
        if (p == null) { // java.lang.Object methods
            if (method.getName().equals("toString")) {
                return interfaceSimpleName + "Proxy";
            }
            return method.invoke(this, args);
        }
        long t0 = System.nanoTime();
        try {
            return db.execute(c -> {
                DbStatement s = new DbStatement(c, p.parsedSql, p.resultMapper, p.parametersNamesMapping, p.useGeneratedKeys);
                if (args != null) {
                    for (DbImpl.BindInfo bi : p.bindings) {
                        List<Integer> sqlIndexes = p.parametersNamesMapping.get(bi.mappedName);
                        if (sqlIndexes == null) {
                            continue;
                        }
                        for (Integer idx : sqlIndexes) {
                            Object arg = args[bi.argumentIdx];
                            Object value = bi.field != null ? bi.field.get(arg) : bi.getter != null ? bi.getter.invoke(arg) : arg;
                            bi.binder.bind(s.statement, idx, value);
                        }
                    }
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
}
