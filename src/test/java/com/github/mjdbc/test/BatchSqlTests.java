package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.sql.BatchSql;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class BatchSqlTests extends BaseSqlTest<BatchSql> {

    //todo: check DbValues in arrays!!
    //todo: check Beans
    //todo: check Inserts

    public BatchSqlTests() {
        super(BatchSql.class, "sample");
    }

    //    @Test
    public void checkBatchModeTestCollection() {
        List<User> origList = getAndCheckOrigList();

        List<Integer> ids = origList.stream().map(u -> u.id.getDbValue()).collect(Collectors.toList());
        sql.batchUpdateWithCollection(ids);
        checkUpdated();
    }

    //    @Test
    public void checkBatchModeTestIterator() {
        List<User> origList = getAndCheckOrigList();

        List<Integer> ids = origList.stream().map(u -> u.id.getDbValue()).collect(Collectors.toList());
        sql.batchUpdateWithIterator(ids.iterator());
        checkUpdated();
    }

    //    @Test
    public void checkBatchModeTestPrimitiveArray() {
        List<User> origList = getAndCheckOrigList();

        int[] ids = origList.stream().mapToInt(u -> u.id.getDbValue()).toArray();
        sql.batchUpdateWithPrimitiveArray(ids);
        checkUpdated();
    }

    //    @Test
    public void checkBatchModeTestObjectArray() {
        List<User> origList = getAndCheckOrigList();

        Integer[] ids = origList.stream().map(u -> u.id.getDbValue()).toArray(Integer[]::new);
        sql.batchUpdateWithObjectArray(ids);
        checkUpdated();
    }

    private void checkUpdated() {
        List<User> updatedList = sql.selectAllUsers();
        assertEquals(2, updatedList.size());
        assertEquals(100, updatedList.get(0).score);
        assertEquals(100, updatedList.get(1).score);
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
