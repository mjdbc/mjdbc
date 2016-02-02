package com.github.mjdbc.test;

import com.github.mjdbc.test.asset.model.Gender;
import com.github.mjdbc.test.asset.model.User;
import com.github.mjdbc.test.asset.sql.BatchSql;
import com.github.mjdbc.test.asset.sql.error.IllegalBatchArgumentTypeSql;
import com.github.mjdbc.test.asset.sql.error.MultipleBatchParams1Sql;
import com.github.mjdbc.test.asset.sql.error.MultipleBatchParams2Sql;
import com.github.mjdbc.test.asset.sql.error.NonVoidBatchMethodSql;
import com.github.mjdbc.test.asset.sql.error.WildcardBatchParamSql;
import com.github.mjdbc.test.util.ProfiledPreparedStatement;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.ArrayList;
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

    @Test
    public void checkBatchInsert() {
        List<User> newUsers = new ArrayList<>();
        ProfiledPreparedStatement.executeBatchCalls = 0;
        ProfiledPreparedStatement.addBatchCalls = 0;
        newUsers.add(createUser("test1"));
        newUsers.add(createUser("test2"));
        newUsers.add(createUser("test3"));
        newUsers.add(createUser("test4"));
        newUsers.add(createUser("test5"));
        newUsers.add(createUser("test6"));
        newUsers.add(createUser("test7"));
        newUsers.add(createUser("test8"));

        sql.batchInsert(newUsers);

        assertEquals(8, ProfiledPreparedStatement.addBatchCalls);
        assertEquals(4, ProfiledPreparedStatement.executeBatchCalls);

        List<User> usersInDb = sql.selectAllUsers();
        assertEquals(10, usersInDb.size());
        assertEquals(1, usersInDb.stream().filter(u -> u.login.equals("test5")).count());
    }

    private User createUser(String login) {
        User u = new User();
        u.login = login;
        u.firstName = "FirstName";
        u.lastName = "LastName";
        u.gender = Gender.FEMALE;
        u.registrationDate = new Timestamp(System.currentTimeMillis());
        return u;
    }


    @Test(expected = IllegalArgumentException.class)
    public void checkNonVoidBatchMethodThrowsException() {
        db.attachSql(NonVoidBatchMethodSql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkMultipleBatchParams1ThrowsException() {
        db.attachSql(MultipleBatchParams1Sql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkMultipleBatchParams2ThrowsException() {
        db.attachSql(MultipleBatchParams2Sql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIllegalBatchArgumentTypeThrowsException() {
        db.attachSql(IllegalBatchArgumentTypeSql.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkWildcardBatchParamThrowException() {
        db.attachSql(WildcardBatchParamSql.class);
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
