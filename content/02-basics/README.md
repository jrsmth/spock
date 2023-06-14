# Basics

<br>

## What is Spock?
* Spock is a framework for writting expressive tests
* It helps bridge the gap between your requirements and your codebase
    * Spock allows you to define tests that act as runnable documentation for your code
        * TaD: Tests as Documentation, both what the code does and what the requirements are
            * Tests are the missing link in a sense, between what your code is supposed to do and what it actually does
* Spocks helps with TDD:
    * Write your requirements
    * Write your tests as a requirement, using expressive Spock syntax
    * Code your implementation to pass the test
* Compatability:
    * Spock is compatible with JUnit's test runner and therefore can run anywhere that JUnit can
    * Either Maven or Gradle can be used to manage a Spock codebase
        * However Gradle makes the most sense, as we are already using Groovy
* Expressiveness:
    * This the central axiom at the heart of Spock:     
        * Tests should be expressive and easy to read
        * When this is achieved, your test base can accurately describe the requirements of your code

<br>

## Specification Anatomy
* Specification:
    * A "test" class in Spock is referred to as a `Specification` and extends `spock.lang.Specification`
    * By convention, Specification are suffixed with `*Spec`
        * Rather than `*Test`, as we would see with JUnit
        * This reinforces the core idea that tests serve as living documentation for your requirements
    * Specifications are `.groovy` classes
* Feature methods:
    * Specifications contain one or more 'feature' methods, that implement the individual test
        * Feature methods are equivalent to test methods in JUnit that get annotated with `@Test`
        * Groovy methods:
            * By default, Groovy methods are `public`
            * The `def` keyword is the idiomatic way to define feature methods
                * Note: we could use `void` instead of `def`, as our feature methods do not return anything and `def` implies that they could
* Blocks:
    * Feature methods can be divivded into blocked, where special semantics are applied, depending on the label used
    * We can add comments to blocks:
        * In the same way that feature methods take strings as names, blocks can take an optional string
        * This enhances our "TaD" (Tests-as-Documentation) by elaborating on the requirement that the test represents
    * Labels:
        * Main 3 (BDD Test Structure):
            * `given`: establish test context; create the objects to be used, etc
            * `when`: exercise the behaviour of the unit/system under test
            * `then`: contains our assertions; plus any mock actions that are required
        * Supporting:
            * `setup`: equivalent to `given`
            * `expect`: equivalent to `then`
            * `clean up`: safe clean up of resources, independent of assertion pass/fail
            * `where`: allows for parameterised feature methods
            * `and`: extends the proceeding block to make tests easier to read (enhancing "TaD")
    * Note:
        * `when` and `then` blocks must always come in a pair, as they describe the stimuli and expected response
            * See the Spock Block [docs](https://spockframework.org/spock/docs/1.3/all_in_one.html#_blocks)
        * `expect` blocks can be used anywhere in a feature method (not just at the end)
            * Perhaps you wish to verify the pre-conditions of your test and so would use `expect:` alongside/instead of a `given:` block
                * Example: [`UserSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/UserSpec.groovy)
        * Interesting fact: Spock labels are based off of Java labels (for breaking loops, etc)
* Assertions:
    * Spock feature methods do not require an assertions API because conditions are written as plain boolean expressions within a `then:` or `expect:` block
        * More precisely, a condition may also produce a non-boolean value, which will then be evaluated according to Groovy truthiness

<br>

## Specification Lifecycle
* To avoid duplicate code, we can externalise test objects from feature methods, as fields of our specification class
    * There they can be shared amongst all of the feature methods of this specification
    * Such fields are re-intialised before each feature method is run and so each test gets a fresh instance
* Fixture Methods:
    * `setup()`:
        * Runs before each feature method; equivalent to JUnit's `@Before`
    * `cleanup()`:
        * Runs before after feature method; equivalent to JUnit's `@After`
    * `setupSpec()`:
        * Runs before the first feature method; equivalent to JUnit's `@BeforeClass`
    * `cleanupSpec()`:
        * Runs after the last feature method; equivalent to JUnit's `@AfterClass`

<br>

## Comprehensibility
* The importance of readability:
    * It pays to have a readable test suite, as it will enhance the efficacy of your tests as runnable documentation for what the requirements are ('Tests-as-Documentation', TaD)
    * Note, software engineering is about trade-offs:
        * Avoid redundant noise by considering when it is appropriate to use additional explantory blocks and descriptions, rather than use them by default
* Improving readbility:
    * Adhere to the 'Single Responsibility' principle for feature methods
        * That is, feature methods should focus on testing one specific behaviour only
    * `and:` blocks:
        * Used to separate logically different parts of a block within a feature method
    * Block descriptions:
        * Spock gives us the ability to add block-level comments to our feature methods
            * Block descriptions can be particularly useful in improving the readbility of your test suite post-compilation
        * Example: [`UserSpec.groovy`](../../projects/squawker/src/test/groovy/com/jrsmiffy/spock/squawker/UserSpec.groovy)