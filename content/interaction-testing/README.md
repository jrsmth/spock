# Interaction Testing

<br>

## Overview
* Interaction testing concerns testing how the system-under-test deals with collaborators by using mocks, stubs & spies
* We want to prevent tightly coupling our tests with overusing mocks; its important to know when to use them
    * Beware: this topic is easy to misunderstand

<br>

## Mocks, Stubs & Spies
* 'Mocks' and 'Stubs' are test doubles (or substitutes) for real code that your system-under-test interacts with
    * A 'Collaborator' is another object that has a method invoked by your system-under-test
    * `Mock`:
        * A test double that tracks when its methods are called and the parameters that are passed to them.
        * Tests can verify that calls were made as expected
    * `Stub`:
        * A test double whose methods will return a predetermined response or take a pre-determined action (ex: throw exception)

<br>

## Asserting That a Method is Called
* Note: Due to Groovy's Type Inference, we can define mocks using Spock's `mock(<CLASS>)` factory method
    * We have the choice of defining our test doubles with `def dataStore = Mock(DataStore)` or `DataStore dataStore = Mock()`
* We can assert whether a particular method is called on a mocked object and specify the number of times it should be invoked
* Example:
    ```groovy
        def dataStore = Mock(DataStore)
        @Subject user = new PersistenceUser(dataStore, "spock", now())

        def "following another user is persisted"() {
            given:
            def other = new User("kirk")

            when:
            user.follow(other)

            then:
            1 * dataStore.follow(user, other)
            // here we assert the cardinality of the call should be '1'
        }
    ```
* The position of mock verifications:
    * In most Java-based mocking libraries, the set up of the expected method invocations appears before the behaviour is exercised
        * In Spock, the expected method invocations are conventionally put in the `then:` block, to improve readability (i.e all assertions are in the same place)

<br>

## Declaring a Return Value From a Mocked Method
* We can use the right-shift operator `>>` to configure the return value from a method of a mocked object
* Example:
    ```groovy
        def "the following list is read from the database and cached"() {
            given:
            def otherUsers = ["kirk", "bones", "scotty"].collect {
                new User(it, now())
            }

            when: "the list of followed users is requested multiple times"
            def result1 = user.following
            def result2 = user.following

            then: "the database is queried only once"
            1 * dataStore.findFollowing(user) >> otherUsers
            // here, we specify that 'otherUsers' should be returned in our test when the findFollowing() method is called with 'user'

            and: "both calls return consistent results"
            result1 == otherUsers as Set
            result2 == result1
        }    
    ```
* If no return value is specified and method is not void, a default 'zero' value is returned:
    * Depending on the method return type:
        * Booleans :: `false`
        * Numeric Primitives :: `0`
        * Objects :: `null`

<br>

## Strict Mocking
...

