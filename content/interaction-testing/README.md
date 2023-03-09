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
* Note: Due to Groovy's Type Inference, we can define mocks and stubs using their respective...