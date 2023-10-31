# Extending Spock

<br>

## Overview
* Spock offers a good selection of built-in features and also offers some useful ways to extend functionality:
    * Namely, using Hamcrest matchers, JUnit rules and Spock's own extension mechanism

<br>

## Hamcrest Matchers
* Spock supports the use of Hamcrest matchers in the form of `expect()` and `that()`:
    * These are functionality the same and are employed to enhance readability where they make grammatical sense:
        * Typically, `expect()` reads better in a `then:` block
        * Typically, `that()` reads better in an `expect:` block
* Hamcrest matchers can also be used as predicates for parameters in mock or stub interactions
* Note, in general, we should opt for static imports that reduce noise in our feature methods

<br>

## JUnit Rules
* As Spock runs using JUnit's test runner, JUnit rules are compatible with Spock Specifications
* Rules extend the `@Before` and `@After` semantics of JUnit:
    * They are fields in a test class that have an annotation to instruct JUnit how the rule fits in to the test lifecycle:
        * `@Rule` is executed before/after each test
        * `@ClassRule` is executed before the first test / after the last test
* We can define our own rules by implementing the `TestRule` interface:
    * Example: [`TruncateTablesRule`](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-cleanup/src/test/java/squawker/cleanup/v2/TruncateTablesRule.java)

<br>

## Spock Extensions
* Check the docs if you are interested:
    * https://github.com/spockframework/spock/blob/master/docs/extensions.adoc

