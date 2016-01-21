package com.github.mjdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Proxy for Dbi methods. Wraps them with transactional context by utilizing db.execute(DbOp) method.
 */
class DbiProxy<T> implements InvocationHandler {
    private final DbImpl db;
    private final T impl;

    DbiProxy(DbImpl db, T impl) {
        this.db = db;
        this.impl = impl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long t0 = System.nanoTime();
        try {
            //noinspection unchecked
            return db.execute(c -> (T) method.invoke(impl, args));
        } finally {
            db.updateTimer(method, t0);
        }
    }
}
