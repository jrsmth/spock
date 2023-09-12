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