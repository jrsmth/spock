# Idiomatic Spock

<br>

## Overview
* To make tests as effective and expressive as possible, it is worth learning how to leverage the full power of both Spock & Groovy
    * Be aware not to write verbose, Java-like, JUnit-esque tests, when a more idiomatic Spock approach is available
    * Idiomatic, /ˌɪd.i.əˈmæt.ɪk, using expressions that are natural to a native speaker
        * As an apprentice of Spock, one must embrace his inner *Vulcan*
* One of the most !mportant rules of writting tests:
    * `Never trust a test that you have not seen fail.` - else you risk not knowing what the test actually does
        * What this quip really means:
            * 'Never trust a test where you don't understand exactly what is tested'
                * A good way to tell is to purposefully try to make the test fail and verify your understanding

<br>

## The "Cuckoo" Anti-Pattern
* **TLDR:** 1:1 relationship between production and test class where 1:many is more appropriate
* The "Cuckoo" is a feature method that sits in a specification where it doesn't really belong
* This is a result of forcing all tests for a specific unit of code to be in the same test class
    * When focusing on testing the behaviour of our app, it can be prudent to group tests in a Spec that cuts across units of code
* When building out your test suite, begin by organising tests in a 1:1 relationship between the production and test classes
    * As your application grows and new features are added, consider adding new specification classes organised around the new feature
        * Rather than bolting tests onto existing specifications
            * In a sense we want to avoid violating the Single Responsiblity SOLID principle
                * Although it is not always wrong to tack on new feature method as the project grows
            * As with many things in Software Engineering, its about trade-offs

<br>

## The "Test-Per-Method" Anti-Pattern
* **TLDR:** 1:1 relationship between production and test method where 1:many is more appropriate
* This is where the developer attempts to squeeze every test for a particular unit of code (e.g. a method) into a single feature method
* It is reasonable to begin with a 1:1 relationship between production and test method
    * However, as the complexity of a code unit grows, we should have multiple feature methods:
        * Each one testing a different path of execution - e.g. happy path, error handling, invalid inputs, etc
* Reminder ad nauseum:
    * Like with "normal" code, test methods should be elegant, read like well-written prose and not violate the principle of single responsibility
* Note:
    * When requirements change the code base, the test base should also reflect similar changes
        * This is because tests document the requirements; as well as documenting what the code actually does (living requirements & documentation, TaD [`Tests-as-Documentation`])
    * However, when the production code is being refactored and requirements stay the same, tests should change as little as possible (ideally not at all)
        * We should decouple the test base from the code base as much as we can; in order to reduce the fragility of our tests

<br>

## Well-Factored Specification
* ...
