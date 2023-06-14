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
* `@Shared`:
    * Marking a Specification field with `@Shared` indicates that it is not reinitialised before each feature method
        * Instead the value is 'shared' between tests
    * I cannot think of a good use-case to mandate the neccesity of `@Shared`; rather I reckon its purpose is for test clarity (expressiveness)
        * That seems to be the opinion on this StackOverflow [post](https://stackoverflow.com/questions/35923714/when-spocks-shared-annotation-should-be-preferred-over-a-static-field)

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
* Within `where:`, we define a set of iterable data; which causes the method to be run once for each data point
    * Example: [`RegistrationSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/registration/RegistrationSpec.groovy)
* Data Pipe:
    * ex: 
        ```groovy
            where: 
            username << [null, '', ' ', '@&%\$+[', 'spock']
        ```
        * Using the 'left-shift' operator (`<<`), we can define a data pipe that passes a set of data points to the `username` variable
        * Note, `username` is not defined out of the `where:` block and yet is available in the remaining body of the test
    * Multiple data pipes:
        * We can have multiple data pipes in a single `where:` block
        * Whilst two is generally acceptable, any more is likely to reduce readability
* Data Table:
    * If we're considering the use of multiple data pipes, it may be more prudent to use a data table instead
        * A data table is little more than semantic sugar over multiple data pipes
            * With the purpose of enhancing the readability and maintainability for our parameterised test data
    * Structure:
        * Column headers serve as variable names
        * Each row represents the data passed in for each iteration of the feature method
    * Example: [`RegistrationSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/registration/RegistrationSpec.groovy)
* Derived Values:
    * Simple assignments can also be used in a `where:` block
    * As is the central axiom of Spock, this is done to make a feature method more expressive:
        * The rule of thumb being, default to using derived values when they enhance the readability of the test
    * Example:
        ```groovy
                @Unroll
                def 'a user only sees messages from users they follow in their timeline'() {
                    given:
                    dataStore.insert(message)

                    expect:
                    user.timeline().contains(message) == shouldAppearInTimeline

                    where:
                    postedBy        | shouldAppearInTimeline
                    user            | true
                    followedUser    | true
                    notFollowedUser | false

                    message = new Message(postedBy, 'Lorem ipsum dolor sit amet') // Note :: use of a 'derived value'
                }
        ```
* Notes:
    * For conciseness, default to using a data pipe over a data table; only use a table when multiple variables are being parameterised
    * Typically, parameterised data is used to form varying arguments for the subject method that we're exercising

<br>

## `@Unroll`
* If one or more interactions of a parameterised feature method fail, we will only see a single test report failure by default
    * `@Unroll` allows you to generate a separate test report for each execution of the feature method
* The annotation can also be used to enhance the readability of our test reports, by addding a description:
    * ex: 
        ```groovy
            @Unroll('a new user can not register with the username "#username"')
            def 'a new user can not register with an invalid username'() { }
        ```
        * `#username` is a reference to the data pipe variable defined in the 'where:' block
        * Note the combination of single and double quotes; something to remember before you gung-ho `cmd-r` an entire spec's `"`
* Using `@Unroll` in place of a feature method name:
    ```groovy
        @Unroll
        def 'a new user can not register with the username "#username"'() { }
    ```
    * The readability of our specifications is of chief importance and so we should carefully consider when to use `@Unroll` in place of feature method names
        * Do replace:
            * If the `@Unroll` description matches the feature method name or is effective at communicating the purpose of the feature method
        * Don't replace (i.e. keep both):
            * If multiple 'token' variable names are used in the `@Unroll` description and this does not effectively communicate what the purpose of the test is
            * A good rule of thumb:
                * Use the feature method name to describe the general test case and the `@Unroll` description to describe each specific parameterised interaction