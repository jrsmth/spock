# Testing REST APIs

<br>

## Overview
* REST, REpresentational State Transfer, is an architectural style for managing data over HTTP
* `@LocalServerPort int port` will inject the port value that is given via `SpringBootTest.WebEnvironment.RANDOM_PORT`
* `TestRestTemplate` is a HTTP client, provided by Spring Boot, for the purpose creating realistic requests to our REST endpoints
    * Example: [`MessageEndpointSpec.groovy`](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-rest-spring/src/test/groovy/squawker/api/MessageEndpointSpec.groovy)

<br>

## Dealing with Test Data
* Java's H2 in-memory database is a good candidate for holding your test data because it is disposable and won't stick around after the test suite has ran
    * This helps to alleviate the risk of "bleeding data" between tests
    * Similarly, we should clean up test data after each feature method, using the `setup`/`cleanup` lifecycle feature methods 