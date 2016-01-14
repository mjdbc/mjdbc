package com.github.mjdbc;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public interface BatchIteratorFactory {
    BatchIteratorFactory ITERATOR_HANDLER = iterator -> new Iterator() {
        final Iterator it = (Iterator) iterator;

        @Override
        public Object next() {
            return it.next();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }
    };
    BatchIteratorFactory ITERABLE_HANDLER = o -> ITERATOR_HANDLER.createIterator(((Iterable) o).iterator());

    BatchIteratorFactory ARRAY_HANDLER = array -> new Iterator() {
        int idx = -1;
        final int length = Array.getLength(array);

        @Override
        public Object next() {
            if (idx >= length - 1) {
                throw new NoSuchElementException();
            }
            idx++;
            return Array.get(array, idx);
        }

        @Override
        public boolean hasNext() {
            return idx < length - 1;
        }
    };

    @NotNull
    Iterator createIterator(@NotNull Object o);
}
