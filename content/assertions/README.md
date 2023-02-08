# Assertions

<br>

## Overview
* Goals for writting assertions (ordered by importance):
    1. Good Diagnostics
        * The test report should be very specific on why the test failed
    2. Expressiveness
        * The test case should be easy to understand
    3. Conciseness
        * The test should be easy to read
* TDD Mantra:
    * "red, green, refactor"
        * Write the tests first, such that they fail; write the implementation so the test passes; then refactor both tests and code
* You should write tests defensively:
    * That is, ensuring that they fail for the right reasons; this is why good diagnostics is so important for tests
* Golden rule of testing:
    * Never trust a test that you haven't seen fail!

<br>

## Power Assert
* Groovy's `assert` was adopted from Spock
    * It offers us valuable diagnostics in the terminal
    * Due to the diagnostics, it us more powerful that Java's `assert` that is used by JUnit
        * Which was intially designed to enforce invariants in production-systems, rather than for unit testing
    * [Good Article](https://blog.nareshak.com/groovy-power-asserts/)
* With Spock, in any `then:` or `expect:` block, a boolean expression is treated as an assertion
    * By convention, we omit the `assert` keyword
* We can implement `.toString()` to improve the diagnostics of the power assert:
    * Example: [`User.java`](../../projects/squawker/src/main/java/com/jrsmiffy/spock/squawker/User.java)

<br>

## Assertions & Loops
* Spread Operator:
    * The "Spread" operator (`*.`), otherwise known as the "Spread-dot" operator, is used to call an action on each item in an aggregate object
        * In other words, we can call a method on each item in a list
        * [Docs](https://groovy-lang.org/operators.html#_spread_operator)
    * Example: [`UserSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/UserSpec.groovy)
* Looping over assertions:
    * Long story short: don't loop over test criteria, as the tests will pass but they will be treated as booleans rather than assertions.
        * Consider using Groovy's `every` method instead
            * Example: [`UserSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/UserSpec.groovy)
* Grouping Assertions on the Same Object
    * Spock allows you to group together assertions on an object with `with(Object, Closure)`
        * Example: [`UserSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/UserSpec.groovy)
    * Beware not to confuse Spock's `with()` and Groovy's `with()`

<br>

## Expecting Assertions
* When writting tests, it important to test both the happy path through the component, as well as the potential failure points
    * The common way to deal with these failure points is to raise exceptions
* In Spock, we can assert that an exception should be thrown from the behaviour that is being tested in our `when:` block
    * We do this by specifying `thrown(ExcpetionType)` in the following `then:` block
        * Where `ExceptionType` is of type `Class <E extends Throwable>`
    * Example: [`UserSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/UserSpec.groovy) 
* It can be useful to further interrogate caught exceptions:
    ```groovy
        def e = thrown(IllegalArgumentException) // first assertion
        e.message == '...' // second assertion
    ```