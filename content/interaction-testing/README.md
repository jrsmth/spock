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
* A 'Spy' allows us to perform interaction testing but is not a mock / dummy of a real object
    * Rather, it wraps an actual object and intercepts method calls to it
        * Thus, allowing you to provide your own implementations
        * Where a method implementation is not provided, calls are delegated to the original object
            * In this sense, a Spy can act like both a standard object or a test double (Mock / Stub)
        * Due to this, a Spy cannot be used on an interface (only a standard concrete class)
    * Note: Spies are not covered in 'Spock: Up & Running, `v1.1`' as they were introduced in `v1.3`

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
            // 'otherUsers' is returned in our test when the findFollowing() method is called w/ 'user'

            and: "both calls return consistent results"
            result1 == otherUsers as Set
            result2 == result1
        }    
    ```
* If no return value is specified and method is not void, a default 'zero' value is returned:
    * 'Zero value' per return type:
        * Booleans :: `false`
        * Numeric Primitives :: `0`
        * Objects :: `null`

<br>

## Strict Mocking
* Spock Mocks are 'lenient' in so far as they allow calls that were explicitly expected
    * Unexpected calls to non-void methods return a 'zero value'
* Lenient mocking is generally an advantageous feature of Spock, as it allows us to ignore incidental behaviours and focus on the test
    * However, sometimes 'strict' mocking is required, where we specifically require the cardinality of a certain method call to be `0`
        * This can be demonstrated in the context of Squawker to ensure that no data is written to database if certain conditions are broken:
            ```groovy
                  def "a message that is too long is not written to the database"() {
                    given: "some message text that exceeds the maximum allowed length"
                    def messageText = """On my planet, 'to rest' is to rest, to cease using
                                        energy. To me it is quite illogical to run up and down
                                        on green grass using energy instead of saving it."""

                    expect:
                    messageText.length() > Message.MAX_TEXT_LENGTH

                    when: "a user attempts to post the message"
                    user.post(messageText, now())

                    then: "an exception is thrown"
                    thrown(IllegalArgumentException)

                    and: "no attempt is made to write the message to the database"
                    0 * dataStore.insert(_)
                }
            ```

<br>

## Wildcards
* Wildcards aid us in decoupling our tests from the implementation details:
    * Parameters:
        * `_` : Any single value of any type
        * `_ as <TYPE>` : Any single value of `<TYPE>`
        * `*_` : Any number of values (zero or more) of any type
    * Methods:
        * `0 * mockObject._` : assert there are `0` calls to any method on `mockObject`
        * `0 * _` : assert there are `0` calls to any method on any mock

<br>

## Defining Behaviour for Mocked Methods
* As mentioned above, we can use the right-shift operator (`>>`) to define the return value for a mocked method
    * Similarly, we can use a Groovy Closure to define a behaviour as well; such as throwing an exception
        * Example:
            ```groovy
                def "an exception is thrown if the database connection is stale"() {
                    when:
                    user.posts

                    then:
                    1 * dataStore.postsBy(user) >> {
                        throw new UnableToCreateStatementException(null)
                    }

                    and:
                    def e = thrown(IllegalStateException)
                    e.cause instanceof UnableToCreateStatementException
                }
            ```

<br>

## Stubs
* Removing Invocation Constraints:
    * Stubs are like Mocks in so far as they emulate the behaviour of an object
        * However, they differ in that Stubs do not verify whether or not they are interacted with
            * They only provide pre-defined method behaviours in the event that they are interacted with
    * Example:
        * In an example above, we considered a test where an exception is thrown from the method of a test double
            * However, we do not need to be making the assertion that the cardinality of invocation is `1`
                * We could use a wildcard to assert for any number of invocations (i.e `_ * dataStore.postsBy(user) >> {...}`)
                    * Or for readability, we could use a Stub to define `dataStore` (i.e `def dataStore = Stub(DataStore)`):
                        ```groovy
                          def "an exception is thrown if the database connection is stale"() {
                                given:
                                dataStore.postsBy(user) >> {
                                    throw new UnableToCreateStatementException(null)
                                }

                                when:
                                user.posts

                                then:
                                def e = thrown(IllegalStateException)
                                e.cause instanceof UnableToCreateStatementException
                            }
                        ```
                        * Unlike the method behaviour of a Mock (which is defined in `then:`), the method behaviour for the Stub is defined in `given:`

<br>

## The Dark Art of Selecting a Test Double (Mock vs Stub vs Spy)
* When to use a `Mock()`:
    * Use a Mock, if the test is concerned with proving that the `@Subject` does in fact interact with a collaborator in a certain way
        * The danger of over-using Mocks, in place of Stubs, is that we create tests which are overly strict
        * Use a Mock, if the collaborator is an 'output':
            * That is, the interaction with the test double is required to prove that your `@Subject` is working correctly
* When to use a `Stub()`:
    * Use a Stub, if the collaborator exposes particular behaviours in your `@Subject` and the resulting outcome is what you are testing
        * Use a Mock, if the collaborator is an 'input':
            * That is, the interaction with the test double does something (returns a value, throws an exception, etc) that causes the behaviour that you want to test
        * GRoT: if you neeed to define the behaviour of the test double, you *probably* need a Stub
* Mixing Mocks & Stubs:
    * Spock's Mocks & Stubs are interachangeable; an object can even be both simultaneously
        * This allows us to stub certain methods of an object and mock others
* When to use a `Spy()`:
    * A Spy should be used to solve specific problems in Spock and therefore is only be used when absolutely necessary
    * In general, thinking you need a `Spy()` for common test cases is a code smell for a `@Subject` or Specification that should be refactored
* Order of Use:
    * If you need a test double, consider which one to choose in this order (from lowest complexity to highest)
        1. `Stub()` :: default choice for a test double (to provide dummy behaviour)
        2. `Mock()` :: use where its necessary to verify the number of invocations of a method
        3. `Spy()` :: use where its necessary to also call the collaborator directly (blurring the lines of your unit test)