# Managing Resources

<br>

## Persistence Testing
* Testing persistence is a frequent use case in integration testing
    * When performed incorrectly, it can render your test suite ineffective through slow and brittle tests
* A common anti-pattern is to test everything by reference to a single monolithic fixture
    * That contains an ever growing volume of data which attemots to cater for every use case
        * Such a fixture does not scale well
    * You want to avoid a situation where a test suite takes hours to run
        * As well as no one being sure whether or not passing tests actually means the code is working correctly
* General rules of thumb:
    * Set up the minimum amount of data required for a specific test (no excess data)
    * Keep everything in-memory AMAP (H2 by default)
* Example: Storing a User object
    ```groovy
        Handle handle
        @Subject DataStore dataStore // custom object that handles SQL updates via JDBI

        def 'can insert a user object'() {
            given:
            def clock = Clock.fixed(now(), UTC)

            and:
            def user = new User("spock", clock.instant())

            when:
            dataStore.insert(user)

            then:
            def iterator = handle.createQuery("select username, registered from user").iterator()
            iterator.hasNext() // assert that the first row exists

            with(iterator.hasNext()) {
                username == user.username
                registered.time == clock.instant().toEpochMilli()
            }

            and:
            !iterator.hasNext()
        }
    ```

<br>

## Managing Resources with the Spock Lifecycle
* Consider using `setup()` and `cleanup()` when working with managed resources, such as a database:
    * `setup()`: 
        * Acquire connection to the database
        * Configure object-relational mapping
        * Create DAO under test
        * Ensure the required tables actually exist
    * `cleanup()`:
        * Clean up the data that was created as part of the test
        * Dispose of the database connection properly

<br>

## Test Leakage
* Unit tests must be idempotent:
    * Meaning that the test should produce the same result each time it is run
        * The number of times of execution is idenpedent from the result (which should always be the same)
    * Two things can be said about idempotent tests:
        1. The order of execution of the test suite is irrelevant to the outcome
        2. A group of tests can be run sequentially and have the same outcome as when the tests are each run in isolation
* Test Leakage is where side effects from one test affect subsequent tests in the suite:
    * Test Leakage can be very difficult to diagnose, as the cause will likely not show in the logged result of that test
    * Test Leakage is often caused by badly managed resources:
        * Left over data in a persistent store
        * Reused mocks
        * Uncontrolled changes to global state (such as a change to the system clock)
        * Changes to a Class' metadata
* `@Shared`:
    * The `@Shared` annotation marks a field where you want to share a single object across each test within a Spec
    * Fields marked with `@Shared` are only initialised once
        * Unlike standard fields in a specification, which are re-initialised after each feature method
            * `@Shared` fields are initialised before the first feature method is run
    * Designed to be used in conjuction with `setupSpec()` and `cleanupSpec()`, rather than `setup()` and `cleanup()`
        * It is functionally equivalent to initialising the field at the very beginning of `setupSpec()`
    * `@Shared` vs `static` [discussion](https://stackoverflow.com/questions/35923714/when-spocks-shared-annotation-should-be-preferred-over-a-static-field):
        * TLDR: Spock is about expressiveness and `@Shared` means what it says on the tin
* `expect:`
    * As mentioned previously, an `expect:` block can be used at the start of a feature method to verify the preconditions of a test
    * Using `expect:` (where appropriate) is a good bit of defensive coding that allows you to watch out for test leakage

<br>

## Specification Inheritance
* Specification hierachies can be created in order to reduce duplicate logic
* At a high-level:
    * The execution of `setupSpec` and `setup()` proceeds down the inheritance tree
    * The execution of `cleanupSpec` and `cleanup()` proceeds up the inheritance tree
* Note:
    * You have to 'manually' call `super.setup()` in the child classes, as Spock handles this automatically