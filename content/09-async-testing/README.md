# Asynchronous Testing

<br>

## Overview
* A test is said to deal with asynchronous data if actions occur on a different thread to the one that initiated the test
* Failure to handle such behaviour properly will lead to inaccurate tests that are slow, brittle and prone to leaking state
* The fundamental issue at play is delaying an assertion until all threads have completed execution
* There are two main approaches when it comes to asynchronous tests:
    * Blocking
    * Polling

<br>

## Blocking Constructs
* Blocking Constructs are used to capture values generated from asynchronous processes
* `BlockingVariable` & `BlockingVariables`:
    * `spock.util.concurrent.BlockingVariable`:
        * Captures a single value
        * Has `get()` & `set()` methods; where the `get()` operation blocks the test until `set()` is called from the concurrent thread
    * `spock.util.concurrent.BlockingVariables`:
        * Captures multiple values
        * Provides a map whose `get()` operation blocks until `set()` is called for that individual key
            * `BlockingVariables` awaits for each key independently

<br>

## Testing Nonblocking APIs by Using Callbacks
* A typical use for `BlockingVariable` is to substitute for a callback parameter when testing a non-blocking method:
    * A non-blocking method is one that does not make the caller wait for the result, which might take some time to generate
    * Non-blocking methods that generate a result will typically return a `Future` or accept a callback parameter
* Examples:
    * [Naive Implementation](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-async/src/test/groovy/squawker/jdbi/async/AsyncMessageStoreSpec.groovy)
    * [Using `BlockingVariable`](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-async/src/test/groovy/squawker/jdbi/async/AsyncMessageStoreSpec2.groovy)
    * [Using `BlockingVariable` with Closure](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-async/src/test/groovy/squawker/jdbi/async/AsyncMessageStoreSpec3.groovy)

<br>

## Polling For Eventual State
* Spock provides an alternative to Blocking Constructs for async testing, through the `PollingConditions` class:
    * It captures values generated asynchronously by polling for an expected result
    * Two methods are supported:
        * `within(double, Clousre)`
        * `eventually(Closure)`
* Examples:
    * [`PollingConditions`](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-async/src/test/groovy/squawker/jdbi/async/AsyncMessageStoreSpec6.groovy)
    * [`PollingConditions` with Delegate](https://github.com/robfletcher/spock-up-and-running/blob/master/code/squawker-async/src/test/groovy/squawker/jdbi/async/AsyncMessageStoreSpec7.groovy)