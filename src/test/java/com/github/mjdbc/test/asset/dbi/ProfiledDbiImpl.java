package com.github.mjdbc.test.asset.dbi;

public class ProfiledDbiImpl implements ProfiledDbi {
    public int counter1;
    public int counter2;

    @Override
    public void counter1() {
        counter1++;
    }

    @Override
    public void counter2() {
        counter2++;
    }
}
