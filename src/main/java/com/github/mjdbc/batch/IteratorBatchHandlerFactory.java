package com.github.mjdbc.batch;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class IteratorBatchHandlerFactory implements BatchHandlerFactory<Iterator> {
    @NotNull
    @Override
    public BatchHandler createHandler(Iterator it) {
        return new BatchHandler() {
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
