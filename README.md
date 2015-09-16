__mjdbc__  - Small and efficient JDBC wrapper.

[![Build Status](https://travis-ci.org/mjdbc/mjdbc.svg?branch=master)]	(https://travis-ci.org/mjdbc/mjdbc)

## Building

```
mvn -DskipTests=true clean package install
```

## Maven dependency

Add snapshots repository to your pom.xml file. The project now is in alpha stage so there are no public releases to maven central yet.
```
<repositories>
    <repository>
        <id>ossrh</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

Add project dependency:
```
<dependency>
    <groupId>com.github.mjdbc</groupId>
    <artifactId>mjdbc</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency
```

## Usage

Documentation is under development. Please check [unit tests](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/SamplesTest.java) to see API in action.

You may find useful to see the recommended way of writing [SQL query interfaces](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/asset/UserSql.java) and
[transactions](https://github.com/mjdbc/mjdbc/blob/master/src/test/java/com/github/mjdbc/test/asset/dbi/SampleDbi.java) in tests.

### Requirements

Java8+


### License

This project available under Apache License 2.0.