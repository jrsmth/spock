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
    * When requirements change the code base, the test base should also reflect these changes
        * This is because tests document the requirements; as well as documenting what the code actually does (living requirements & documentation, TaD [`Tests-as-Documentation`])
    * However, when the production code is being refactored and requirements stay the same, tests should change as little as possible (ideally not at all)
        * We should decouple the test base from the code base as much as we can; in order to reduce the fragility of our tests

<br>

## Well-Factored Specification
* A lovely saying, that echoes Uncle Bob's sentiments:
    * "Programs must be written for people to read and, only incidentally, for machines to execute"
* What makes feature methods hard to read:
    * When they contain many lines of set-up that establishes multiple preconditions
    * When they use complex code to retrieve values against which assertions are made
    * When they perform multiple actions in order to exercise the behaviour under test
    * When they need to walk through a number of steps to get the system to the point where the behaviour can be tested
* As Uncle Bob recommends in "Clean Code", we should "extract till we drop":
    * Meaning, we should keep breaking down methods into multiple smaller and simpler methods, until each is so simple that they're trivial to understand
        * Remember: like production code, test code should be elegant and read like well-written prose
    * If our extracted methods are well-named and organised into appropriate packages, the resulting program will read as a layered description of itself
    * This organises our code like a news article; in a polite manner that allows the reader to exit when the details no longer concern them
        * The higher-level methods deal in abstracts, calling lower-level methods that deal with the details
            * It is possible to get a clear understanding of 'what' the program does by just reading the high-level methods
            * The low-level methods are concerns with the 'how'; so we can drill down only when the details concern us
                * We're not forced to drill down into the low-level methods in order to pain-stakingly understand the 'what' of the program

<br>

## Sharing Helper Methods
* Where possible we should extract set-up code into helper methods that can be shared across the feature methods of your specification; similarly, we should look to share set-up logic across specification classes where cross-overs occur
* There are a few different ways of sharing helper methods across specifications:
    * Using `import static`:
        * Define `static` methods in a dedicated 'Fixtures' class and simply add them in the desired spec via `import static`
        * Example:
            ```groovy
                class MessagesFixtures {
                    static void postMessageBy(MessageStore store, User poster) {
                        store.insert(poster, 'dummy text', Instant.now())
                    }
                }

                // In the desired spec
                import static com.package.MessagesFixtures.postMessageBy
                ...
                postMessageBy(store, user)
            ``` 
    * Using Groovy Traits:
        * Groovy introduces 'traits', which are akin to interfaces in Java
            * Similarly to interfaces, traits allow for an approximation to multiple inheritance
            * Like interfaces in Java 8, they can contain non-abstract methods
            * Unlike interfaces, Groovy traits can also be stateful and have their own fields; but they also do not have constructors
        * Example: [`FixturesTrait.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/FixturesTrait.groovy)
            * To make use of the fixture logic, we use the `implements` keyword in the desired specification and simply invoke the members (fields + methods) that we need 
    * Using Delegation:
        * We can also create fixtures methods in a 'delagate' class, that is created as a property of the specification class and annotated with `@Delegate`
            * Here, delegation is a way to import instance methods of an object in much the same way that `import static` imports static methods
                * Groovy's `@Delegate` annotation intercepts any unknown method calls and redirects them to the delegate object
            * Note:
                * Multiple delegates can be used in one specification and should conflicts arise, the first declared delegate is given precedence
                * The Geb test framework uses `@Delegate` to provide specifications with convienient access to `Page` and `Browser` objects
        * Example:
            ```groovy
                class SomeSpec extends Specification {

                    @Delegate FixturesDelagate fixtures

                    def setup() {
                        fixtures = new FixturesDelegate(messageStore, userStore, followingStore, user)
                    }

                    ...
                }

                @TupleConstructor // This is a Groovy annotation that performs a similar role to Lombok's @AllArgsConstructor
                class FixturesDelegate {
                    final MessageStore messageStore
                    final UserStore userStore
                    final FollowingStore followingStore
                    final User user

                    void postMessageBy(User poster) {
                        messageStore.insert(poster, 'aaaa', Instant.now())
                    }

                    ...
                }
            ```