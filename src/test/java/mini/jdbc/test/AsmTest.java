package mini.jdbc.test;

import mini.jdbc.DbFactory;
import mini.jdbc.DbInterface;
import mini.jdbc.DbOpContext;
import mini.jdbc.test.asset.TestObject;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 *
 */
public class AsmTest {

    @Test
    public void testAsm1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        DbInterface res = DbFactory.wrap(TestObject.class, null);
        res.getClass().getField("context").set(res, new DbOpContext());

        System.out.println("FIELD: " + res.getClass().getField("context").get(res));
        System.out.println("GETTER: " + res.getContext());
    }

}
