package mini.jdbc.test;

import junit.framework.Assert;
import mini.jdbc.test.util.DbUtils;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 *
 */
public class SamplesTest {

    @Test
    public void test1() throws SQLException {
        DataSource source = DbUtils.prepareDataSource("sampledb.properties");
        Assert.assertNotNull(source);
    }
}
