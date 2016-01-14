package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.sql.BatchSql;
import com.github.mjdbc.test.asset.sql.BatchSqlErr1;
import com.github.mjdbc.test.asset.sql.BatchSqlErr2;
import com.github.mjdbc.test.asset.sql.BatchSqlErr3;
import com.github.mjdbc.test.asset.sql.BatchSqlErr4;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class BatchSqlTest extends BaseSqlTest<BatchSql> {

    //todo: check Inserts
    //todo: check batchSize?

    public BatchSqlTest() {
        super(BatchSql.class, "sample");
    }

    @Test
    public void checkUpdateWithIterable() {
        List<User> origList = getAndCheckOrigList();

        List<Integer> ids = origList.stream().map(u -> u.id.getDbValue()).collect(Collectors.toList());
        sql.batchUpdateWithCollection(ids, 100);
        checkUpdated(100);
    }

    @Test
    public void checkUpdateWithIterator() {
        List<User> origList = getAndCheckOrigList();

        List<Integer> ids = origList.stream().map(u -> u.id.getDbValue()).collect(Collectors.toList());
        sql.batchUpdateWithIterator(ids.iterator(), 200);
        checkUpdated(200);
    }

    @Test
    public void checkUpdateWithPrimitiveArray() {
        List<User> origList = getAndCheckOrigList();

        int[] ids = origList.stream().mapToInt(u -> u.id.getDbValue()).toArray();
        sql.batchUpdateWithPrimitiveArray(ids, 300);
        checkUpdated(300);
    }

    @Test
    public void checkUpdateWithObjectArray() {
        List<User> origList = getAndCheckOrigList();

        Integer[] ids = origList.stream().map(u -> u.id.getDbValue()).toArray(Integer[]::new);
        sql.batchUpdateWithObjectArray(ids, 400);
        checkUpdated(400);
    }

    @Test
    public void checkUpdateWithBeanIterable() {
        List<User> origList = getAndCheckOrigList();

        origList.forEach(u -> u.score = 500);
        sql.batchUpdateBeanWithIterable(origList);
        checkUpdated(500);
    }

    @Test
    public void checkUpdateWithBeanIterator() {
        List<User> origList = getAndCheckOrigList();

        origList.forEach(u -> u.score = 600);
        sql.batchUpdateBeanWithIterator(origList.iterator());
        checkUpdated(600);
    }

    @Test
    public void checkUpdateWithBeanArray() {
        List<User> origList = getAndCheckOrigList();

        origList.forEach(u -> u.score = 700);
        sql.batchUpdateBeanWithArray(origList.toArray(new User[origList.size()]));
        checkUpdated(700);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBatchSqlErr1() {
        db.attachSql(BatchSqlErr1.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBatchSqlErr2() {
        db.attachSql(BatchSqlErr2.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBatchSqlErr3() {
        db.attachSql(BatchSqlErr3.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkBatchSqlErr4() {
        db.attachSql(BatchSqlErr4.class);
    }

    private void checkUpdated(int expectedScore) {
        List<User> updatedList = sql.selectAllUsers();
        assertEquals(2, updatedList.size());
        assertEquals(expectedScore, updatedList.get(0).score);
        assertEquals(expectedScore, updatedList.get(1).score);
    }

    @NotNull
    private List<User> getAndCheckOrigList() {
        List<User> origList = sql.selectAllUsers();
        assertEquals(2, origList.size());
        assertEquals(0, origList.get(0).score);
        assertEquals(0, origList.get(1).score);
        return origList;
    }

}
