# Advanced `where:` Blocks

<br>

## Overview
* When it has come to using `where:` blocks thus far, we have been dealing with static data providers within our specification:
    * Provided either at the feature method level or with `@Shared` fields at the class-level
* This section contains examples that use providers backed by external data:
    * Spock's `where:` blocks can be driven by data from a variety of sources:
        * Database
        * File system
        * Network connection
        * CSV
        * Spreadsheet

<br>

## Example 1 :: Primary Key Verifier
* A test to verify all tables have a primary key (PK):
    ```groovy
        class PrimaryKeySpec extends Specification {

            @Shared @AutoCleanup Connection connection
            @Shared @AutoCleanup("destroySchema") SchemaBuilder schemaBuilder

            def setupSpec() {
                Class.forName("org.h2.Driver")
                connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "")

                schemaBuilder = new SchemaBuilder(connection)
                schemaBuilder.createSchema()
            }

            private Iterable<String> readTableNames() {
                def list = []
                def tables = connection.metaData.getTables(null, null, "%", ["TABLE"] as String[])
                try {
                while (tables.next()) {
                    list << tables.getString(3)
                }
                } finally {
                tables.close()
                }
                list.asImmutable()
            }

            @Unroll
            def "the #tableName table has a primary key"() {
                expect:
                keys.next()

                cleanup:
                keys.close()

                where:
                tableName << readTableNames()
                keys = connection.metaData.getPrimaryKeys(null, null, tableName)
            }
        }
    ```

<br>

## Example 2 :: Static Link Checker
* A test to verify that the static links aren't broken on a webpage:
    * https://github.com/robfletcher/spock-up-and-running/tree/master/code/site/src/test/groovy/linktest
* Notes:
    * JBake:
        * https://github.com/jbake-org/jbake
        * Static site generator for the JVM
    * `File.traverse()`:
        * Groovy adds a `.traverse()` method to Java's `java.io.File`; allowing one to recursively traverse a directory sub-tree
    * Jerry:
        * Part of the Jodd library (which provides a lightweight set of Java tools and utilities)
        * Jerry is a component for HTML document parsing and manipulation that closely mirrors the JQuery API
        * Helpful tip:
            * Make Jerry even more JQuery-like with the following import: `import static jodd.jerry.Jerry.jerry as $`
    * Unirest:
        * http://kong.github.io/unirest-java/
        * Simple HTTP client library, which useful for URL scraping
    * `Iterable`:
        * Any class which implements the `Iterable` interface can be passed into a data pipe within a `where:` block