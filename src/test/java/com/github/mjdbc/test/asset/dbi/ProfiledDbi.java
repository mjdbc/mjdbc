package com.github.mjdbc.test.asset.dbi;

import com.github.mjdbc.Tx;

public interface ProfiledDbi {

    @Tx
    public void counter1();

    public void counter2();
}
