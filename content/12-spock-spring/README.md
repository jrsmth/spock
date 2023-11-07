# Spock & Spring

<br>

## Overview
* Testing Pyramid:
    * There exists three levels of automated testing for our applications:
        * Unit Tests
        * Integration Tests (IT)
        * End-to-end Tests (E2E)
    * These can be represented as a pyramid, where the width of the level denotes the relative amount of tests
        * In other words, we should have many more unit tests than integration tests (IT's); and many more IT's than E2E tests

        <br>

        <img src="./res/test-pyramid.png" width="400">

        <br>
    
    * As we move up the test pyramid, the resolution of our tests with respect to the implementation should decrease:
        * We want to avoid duplicating test logic by focusing on fine details in our unit tests and only looking at the bigger picture in our IT and E2E tests
    * Definitions:
        * Unit Tests:
            * Where we test the behaviour of a distinct unit of our application, in isolation
            * This is typically at the class-level; we test each method call that is exposed by the class, with collaborating classes being mocked
                * Here, the class is tested as a closed chain
        * Integration Tests:
            * Where we test the interactions between units of our application
            * This is typically at the microservice-level; we test each endpoint that is exposed by the service
                * Here, the microservice is tested as a closed chain
        * End-to-end Tests:
            * Where we test the end-user functionality of our application
            * This is typically at the application-level (usually via the webapp); we test each functionality that is exposed to the user, from the input all the way down through the layers of processing and back to the output
                * Here, the application is tested as a closed chain
* Container Testing:
    * This is a branch of integration testing that focuses purely on exercising the interactions a service (e.g a single Java application) has with the underlying container or framework on which it runs
    * This is to ensure that framework features have been properly understood and configured appropriately
    * Mr Fletcher gives the example of using the `@Transactional` feature with Spring as the underlying framework
        * `@SpringBootTest` is used to start the Spring Container and initialise the application context
* Should you split IT and unit tests into separate suites?
    * Some schools of thought call for this on the grounds that integration tests can be many times slower to run that unit tests
    * Mr Fletcher argues that if there is such a drastic time expensive in running our integration tests, we should consider refactoring them
        * Hence, don't worry about splitting into separate IT and unit suites

<br>

## Testing Annotation-Driven Transactions
* Transactions 101:
    * Datebase transactions are groups of operations that are executed as a single atomic unit; any changes will be rolled back in the event that one of operations fails
        * This is so that none of the operations take affect unless the whole process succeeds - protecting the validity of the database
    * `@Transactional`:
        * When applied to a Spring-managed Bean, this annotation wraps invocations of a method call in a proxy that begins the transaction when the method is called and commits it upon successful execution.
            * If an exception is thrown during execution, the transaction is automatically rolled back
        * This is an example of AOP (Aspect-Oriented Programming), a paradigm providing cross-cutting behaviours driven by the presence of annotations
* How to test a transaction rollback:
    * Example: [`MessageServiceTransactionSpec.groovy`](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-spring-integration/src/test/groovy/squawker/mentions/tx/transactional/MessageServiceTransactionSpec.groovy)

<br>

## Application Context
* As mentioned above, `@SpringBootTest` will start up the application context; significantly increasing the resources it takes to run the test
    * `@DataJpaTest` performs a similar role but only stands up parts of the application related to persistence
* Spock offers us `@DirtiesContext`, which can be applied to a specification or feature method to reinitialise the application context after execution:
    * Note, this annotation will affect performance so is to be used to solve the problem of test leaks
        * Remember, Software Engineering is about trade-offs
