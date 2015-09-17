__mjdbc__  - Small and efficient JDBC wrapper.

***mjdbc is:***
* *Small:* no external dependencies. Distribution size is  less than 50kb.
* *Simple:* no special configuration is needed. Start using it after 1 line of initialization code.
* *Reliable:* all SQL statements are parsed and validated during application startup.
* *Flexible:* switch and use native JDBC driver directly when needed.
* *Fast:* no overhead in runtime when compared to JDBC.
* *Transactional:* wrap any method into transaction by adding 1 annotation to it.
* *Extensible:* add support for new data types or override the way built-in types are handled.
* *Measurable:* check timings for all SQL queries and transactions.
* *Open source:* fork and change it.
* *Bug free*... not yet... still in alpha, but number of tests is growing.

[![Build Status](https://travis-ci.org/mjdbc/mjdbc.svg?branch=master)]	(https://travis-ci.org/mjdbc/mjdbc)

## Building

```bash
mvn -DskipTests=true clean package install
```

## Maven

Add snapshots repository to your pom.xml file. The project now is in alpha stage and has no public releases to Central Repository yet.
```xml
<repositories>
    <repository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

Add project dependency:
```xml
<dependency>
    <groupId>com.github.mjdbc</groupId>
    <artifactId>mjdbc</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency
```

## Brief API overview

##### Raw SQL queries
```java
    java.sql.DataSource ds = ...; // have a DataSource first.
    Db db = new Db(ds);  // wrap DataSource with Db class instance.
    db.registerMapper(User.class, User.MAPPER); // register your ResultSet mappers. Do it once on startup.
    MySqlQueries q = db.attachSql(MySqlQueries.class) // attach query interface. It will parse and validate all queries at this moment.
    User user = q.getUserByLogin('login'); // run any query method
```
where MySqlQueries:
```java
public interface MySqlQueries {
    @Sql("SELECT * FROM users WHERE login = :login")
    User getUserByLogin(@Bind("login") String login)
}
```

##### Transactions
To run multiple SQL queries within a single transaction create a dedicated dbi (db-interface) interface with methods that have @Tx annotation.
```java
    java.sql.DataSource ds = ...;
    Db db = new Db(ds);
    MyDbi dbi = db.attachDbi(MyDbiImpl(), MyDbi.class); // all MyDbi method calls will be proxied to MyDbiImpl wrapped with transactions.
    User user = dbi.getUserByLoginCreateIfNotFound('login');
```
where MyDbi:
```java
public interface MyDbi {
    @Tx // @Tx means that this method will be wrapped with connection.begin/commit/rollback(on Exception)
    User getUserByLoginCreateIfNotFound(String login);
}

public class MyDbiImpl implements MyDbi {
    @Tx
    public User getUserByLoginCreateIfNotFound(String login) {
        User user = myQueries.getUserByLogin(login);
        if (user == null) {
            User user = new User();
            user.login = login;
            user.id = myQueries.insertUser(user);
        }
        return user;
    }
}
```
Notes:
 * If @Tx method is called from within another @Tx method no new transaction is started. Child method call shares transaction context with parent method.
 Transaction is committed/rolled back when top-level method is finished.
 * All @Sql (raw SQL) methods are processed as @Tx methods when no dbi interface is used.



##### Result set mappers

Extend [DbMapper](https://github.com/mjdbc/mjdbc/blob/master/src/main/java/com/github/mjdbc/DbMapper.java) class.
It may be convenient to put the implementation into the Java class it maps [(example)](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/asset/model/User.java).

```java
public static DbMapper<User> MAPPER = (r) -> {
    User user = new User();
    user.id = new UserId(r.getInt("id"));
    user.login = r.getString("login");
    ...
    return user;
};
```

Register this mapper in Db interface during initialization;
```java
    Db db = new Db(ds);
    db.registerMapper(User.class, User.MAPPER)
```
Now use User type in all queries attached to Db instance.
Mappers for native Java types are supported by default [(source)](https://github.com/mjdbc/mjdbc/blob/master/src/main/java/com/github/mjdbc/util/Mappers.java) and can be overridden if needed..


##### Parameter binders
Parameters in @Sql interfaces can be bound with @Bind or @BindBean annotations.
* @Bind maps single named parameter.
* @BindBean maps all parameters from public fields or getters of the object passed as parameter.

Binders for native Java types are supported by default [(source)](https://github.com/mjdbc/mjdbc/blob/master/src/main/java/com/github/mjdbc/util/Binders.java).

In most cases you do not need to create your own binder. All you need is to make your class to implement one of these interfaces: DbInt, DbLong or DbString [(example)](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/asset/model/UserId.java).

##### Low level API
Usage of java.sql.* API is transparent in mjdbc. You can always get statements, connections, result sets and have a full power of native JDBC driver.
Example:
```java
Db db = new Db(ds);
db.execute(c -> { // wraps method into transaction
    try (java.sql.Statement statement = c.sqlConnection.createStatement()) {
        ...
    }
});
```
or if you need named parameters:
```java
Db db = new Db(ds);
User user = db.execute(c -> { // wraps method into transaction
    DbStatement s = new DbStatement(c, "SELECT * FROM users WHERE login = :login", User.MAPPER)
    s.setString("login", login);

    // direct access to JDBC starts here:
    Map<String, Integer> namedParametersMapping = s.parametersMapping; // JDBC supports parameter binding by index only. Here is the mapping.
    java.sql.PreparedStatement ps = s.statement;
    ... // write some low-level code with java.sql.PreparedStatement, like bind data streams, even execute it...

    return s.query();
});
```

Note: that when you use (DbStatement)[https://github.com/mjdbc/mjdbc/blob/master/src/main/java/com/github/mjdbc/DbStatement.java] class it is not necessary to close it manually.
It will be closed automatically when connection is closed (returned to pool). In this example the connection is closed when *db.execute()* method is returned.


##### Timers
For all methods annotated with @Sql or @Tx mjdbc automatically collects statistics:
* method invocation count
* total time spent in the method in nanoseconds.

Check [checkTxTimer/checkSqlTimer](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/SamplesTest.java) tests for details.


##### More
For more examples check [unit tests](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/) to see API in action.

You may find useful to see the recommended way of writing [raw SQL interfaces](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/asset/UserSql.java) and
[transactional database interfaces](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/asset/dbi/SampleDbi.java) in tests.


### Requirements

Java8+


### License

This project available under Apache License 2.0.
