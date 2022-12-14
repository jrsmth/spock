# Basics

<br>

## What is Spock?
* Spock is a framework for writting expressive tests
* It helps bridge the gap between your requirements and your codebase
    * Spock allows you to define tests that act as runnable documentation for your code
        * TaD: Tests as Documentation, both what the code does and what the requirements are
            * Tests are the missing link in a sense, between what your code is supposed to do and what it actually does
* Spocks helps with TDD:
    * Write your requirements
    * Write your tests as a requirement, using expressive Spock syntax
    * Code your implementation to pass the test
* Compatability:
    * Spock is compatible with JUnit's test runner and therefore can run anywhere that JUnit can
    * Either Maven or Gradle can be used to manage a Spock codebase
        * However Gradle makes the most sense, as we are already using Groovy

<br>

## Specification Anatomy
* Specification:
    * A "test" class in Spock is referred to as a `Specification` and extends `spock.lang.Specification`
    * By convention, Specification are suffixed with `*Spec`
        * Rather than `*Test`, as we would see with JUnit
        * This reinforces the core idea that tests serve as living documentation for your requirements
    * Specifications are `.groovy` classes
* Feature methods:
    * Specifications contain one or more 'feature' methods, that implement the individual test
        * Feature methods are equivalent to test methods in JUnit that get annotated with `@Test`
        * Groovy methods:
            * By default, Groovy methods are `public`
            * The `def` keyword is the idiomatic way to define feature methods
                * Note: we could use `void` instead of `def`, as our feature methods do not return anything and `def` implies that they could
* Blocks:
    * Feature methods can be divivded into blocked, where special semantics are applied, depending on the label used
    * We can add comments to blocks:
        * In the same way that feature methods take strings as names, blocks can take an optional string
        * This enhances our "TaD" (Tests-as-Documentation) by elaborating on the requirement that the test represents
    * Labels:
        * Main 3 (BDD Test Structure):
            * `given`: establish test context; create the objects to be used, etc
            * `when`: exercise the behaviour of the unit/system under test
            * `then`: contains our assertions; plus any mock actions that are required
        * Supporting:
            * `setup`: equivalent to `given`
            * `expect`: equivalent to `then`
            * `clean up`: safe clean up of resources, independent of assertion pass/fail
            * `where`: allows for parameterised feature methods
            * `and`: extends the proceeding block to make tests easier to read (enhancing "TaD")
            

Whats the rule about proceeding when, then? (given?) 
