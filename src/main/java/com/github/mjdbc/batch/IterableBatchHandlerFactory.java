package com.github.mjdbc.batch;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class IterableBatchHandlerFactory implements BatchHandlerFactory<Iterable> {
    @NotNull
    public BatchHandler createHandler(Iterable o) {
        return new BatchHandler() {
            Iterator it = o.iterator();

            @Override
            public Object next() {
                return it.next();
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
        };
    }
}
