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

## Specifications
* A "test" class in Spock is referred to as a `Specification` and extends `spock.lang.Specification`
* By convention, Specification are suffixed with `*Spec`
    * Rather than `*Test`, as we would see with JUnit
    * This reinforces the core idea that tests serve as living documentation for your requirements
* Specifications are `.groovy` classes...