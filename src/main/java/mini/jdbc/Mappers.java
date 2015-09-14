package mini.jdbc;

import mini.jdbc.DbMapper;
import mini.jdbc.mapper.IntegerMapper;
import mini.jdbc.mapper.VoidMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Mappers {

    public static final Map<Class, DbMapper> BUILT_IN_MAPPERS = Collections.unmodifiableMap(new HashMap<Class, DbMapper>() {{
        put(Void.TYPE, VoidMapper.INSTANCE);
        put(Void.class, VoidMapper.INSTANCE);

        // primitive types
        put(Integer.TYPE, IntegerMapper.INSTANCE);
        put(Integer.class, IntegerMapper.INSTANCE);
    }});
}
