package com.github.mjdbc.test;

import com.github.mjdbc.Db;
import com.github.mjdbc.DbConnection;
import com.github.mjdbc.test.asset.dbi.SampleDbi;
import com.github.mjdbc.test.asset.dbi.SampleDbiImpl;
import com.github.mjdbc.test.asset.sql.UserSql;
import com.github.mjdbc.test.util.DbUtils;
import com.github.mjdbc.test.util.ProfiledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test to check that transactional context is used correctly and in lazy fashion:
 * only after some real SQL statement is opened.
 */
public class LazyTxTest extends Assert {
    private ProfiledDataSource profiledDs;
    private HikariDataSource origDs;
    private Db db;
    private SampleDbi dbi;
    private UserSql sampleQueries;

    @Before
    public void setUp() {
        origDs = DbUtils.prepareDataSource("sample");
        profiledDs = new ProfiledDataSource(origDs);
        db = Db.newInstance(profiledDs);
        dbi = db.attachDbi(new SampleDbiImpl(db), SampleDbi.class);
        sampleQueries = db.attachSql(UserSql.class);
    }

    @After
    public void tearDown() {
        dbi = null;
        origDs.close();
    }

    @Test
    public void testNoQueryNoConnectionOpenedDbi() {
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 0);
        dbi.getValueUseCacheIfPossible(true);
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 0);
    }

    @Test
    public void testWithQueryConnectionIsOpenedDbi() {
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 0);
        dbi.getValueUseCacheIfPossible(false);
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 1);
    }

    @Test
    public void testWithQueryConnectionIsOpenedSql() {
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 0);
        sampleQueries.countUsers();
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 1);
    }

    @Test
    public void testNoQueryNoConnectionOpenedDbOp() {
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 0);
        db.executeV(c -> {/* do nothing*/});
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 0);
    }

    @Test
    public void testWithQueryConnectionIsOpenedDbOp() {
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 0);
        db.executeV(DbConnection::getConnection);
        Assert.assertTrue(profiledDs.nGetConnectionCalls == 1);
    }

}
