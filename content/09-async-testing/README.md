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