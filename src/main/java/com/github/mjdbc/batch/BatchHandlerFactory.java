package com.github.mjdbc.batch;

import org.jetbrains.annotations.NotNull;

public interface BatchHandlerFactory<T> {
    BatchHandlerFactory ITERABLE = new IterableBatchHandlerFactory();
    BatchHandlerFactory ITERATOR = new IteratorBatchHandlerFactory();
    BatchHandlerFactory ARRAY = new ArrayBatchHandlerFactory();

    @NotNull
    BatchHandler createHandler(T o);
}
