package com.github.mjdbc.batch;

public interface BatchHandler {
    Object next();

    boolean hasNext();
}
