package com.github.mjdbc.batch;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;

public class ArrayBatchHandlerFactory implements BatchHandlerFactory {
    @NotNull
    @Override
    public BatchHandler createHandler(Object o) {
        return new BatchHandler() {
            int idx = -1;
            int length = Array.getLength(o);

            @Override
            public Object next() {
                if (idx >= length - 1) {
                    throw new NoSuchElementException();
                }
                idx++;
                return Array.get(o, idx);
            }

            @Override
            public boolean hasNext() {
                return idx < length - 1;
            }
        };
    }
}
