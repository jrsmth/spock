# Parameterised Specifications

<br>

## Overview
* (Reminder) Ad Nauseam:
    * A Specification class is a description of the behaviour of (system-under-test) class
        * It serves as documentation for what the code should do and actually does (providing tests pass)
* Parameterisation:
    * What:
        * If multiple feature methods are describing the same general behaviour, we may be able to extract a generalised test
            * Such a generalised test could take parameters that serve each of the previously separated test cases
    * Why:
        * We can reduce duplicated logic across the test set up and behaviour exercise
            * This makes our tests more readable but also means that if requirements change, we only need to update the single test
        * Parameterisation solves the anti-pattern of using loops in our test methods, to exercise the behaviour-under-test multiple times within the same execution
    * When:
        * We should look to parameterisation where possible:
            * To reduce code duplication but also to encourage us to extend the coverage of our feature methods
                * By considering what happens when we leave the happy path or have edges cases
    * How:
        * Parameters are supplied in the `where:` block, at the tail of a feature method
        * For each set of parameters, the feature method is executed once

<br>

## Comparison with JUnit
* JUnit offers some annotations to achieve parameterisation:
    * However these are not as flexible, simple-to-use or readable as using Spock's `where:` block
* JUnit 5:
    * `@ParameterizedTest`
* JUnit 4:
    * `@Parameters` + `@RunWith(Parameterized.class)`

<br>

## `where:` block
* Spocks offers us a `where:` block, to be positioned at the end of our feature methods
    * The block's main purpose is to reduce duplicated feature methods of similar behaviour, by grouping similar test data under one method
* Within `where:`, we define an iterable set of data that causes the method to be run once for each data point
    * Example: [`RegistrationSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/registration/RegistrationSpec.groovy)
* Data Pipe:
    * ex: 
        ```groovy
            where: 
            username << [null, '', ' ', '@&%\$+[', 'spock']
        ```
        * Using the 'left-shift' operator (`<<`), we can define a data pipe that passes a set of data points to the `username` variable
        * Note, `username` is not defined out of the `where:` block and yet is available in the remaining body of the test

* Data Table:
    * `// TODO`
* Notes:
    * For conciseness, default to using a data pipe over a data table; only use a table when multiple variables are being parameterised
    * Typically, parameterised data is used to form varying arguments for the subject method that we're exercising

<br>

## `@Unroll`
* This