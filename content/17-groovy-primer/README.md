# Groovy Primer

<br>

## Overview
* Groovy was released in 2007 and syntactically derived from Java
    * However it also takes inspiration from Ruby and Smalltalk
        * [Smalltalk](https://en.wikipedia.org/wiki/Smalltalk) was an object-oriented language from the 80's, where *everything* is an object
* It is a dynamically-typed language; method calls and property reference are dispatched at runtime
    * These can be intercepted by a type's "metaclass" in order to extend the functionality of a class

<br>

## Syntactic and Semantic Comparison with Java
* Semi-colons:
    * Unrequired in Groovy (non-idiomatic)
* Class literals:
    * Unnecessary to use `.class` in Groovy
* Visibility:
    * Groovy methods default to `public` rather than Java's `package-private`
        * Java `package-private` is emulated by Groovy's `@PackageScope`
    * `private` and `protected` behave the same in Groovy as in Java
        * Reminder: `protected` is similar to the Java default modifier in that any class within the same package has access
            * However, `protected` also extends visibility to any child class, even those outside the package
* Exceptions:
    * Groovy doesn't have checked exceptions (i.e those that must be handled or declared)
* Implicit Return:
    * Unless defined as `void`, Groovy methods return the value of the last statement
    * Therefore the `return` keyword is unrequired but can enhance readability
* Default Parameters:
    * Groovy methods can define default values for their parameters:
        * The only restriction is that non-default parameters must come before default ones
* Method Dispatch:
    * Java dispatches calls with compile-time type information, whereas Groovy does so with runtime type information

<br>

## Valid Java Code That Is Not Valid Groovy Code
* Array Initialisation:
    * Array literals are not valid in Groovy:
        ```java
            // Java
            int[] array1 = {1, 2, 3};
            int[] array2 = new int[] {1, 2, 3};
        ```
    * Instead of array literals, Groovy allows us list literals that can be converted into arrays:
        ```groovy
            int[] array = [1, 2, 3] as int[]
        ```
    * This also affects annotation parameters which are written with `[]` rather than `{}`, as with Java
* Multiple `for` Loop Variables:
    * The following is invalid Groovy code:
        ```java
            for (int i = 0, j = 0; i < 5; i++, j++) {}
        ```
* The do... While Loop:
    * The following is not supported in Groovy:
        ```java
            do {
                doSomething();
            } while (somethingNeedsDoing);
        ```
* Try With Resources:
    * Introduced in Java 7, not available in Groovy; use `.withCloseable()` instead
* Lambdas:
    * Introduced in Java 8, not available in Groovy; use Groovy closure instead
* Method References:
    * Introduced in Java 8, not available in Groovy; instead of `::`, Groovy uses `.&`

<br>

## Dynamic-Typed Variables
* The `def` keyword can be used to define parameters, fields, variables and method return types that may be assigned a value of any type
* The type can change of the fly, though this can make for hard to read code
* Note: its idiomatic to use `def`, but static type information should be used when code would be easier to understand

<br>

## List & Map Literals
* Groovy does not have array literals
* It does have `List` and `Map` literals
    * Which greatly improve on how Java lists and maps are created (see examples below)
* Lists:
    * Creating Lists:
        ```Java
            // Java
            List<String> crew = new ArrayList();
            crew.add("Kirk");
            crew.add("Spock");
            crew.add("Bones");

            // or...
            List<String> crew = List.of("Kirk", "Spock", "Bones");
        ```
        ```Groovy
            // Groovy
            def crew = ["Kirk", "Spock", "Bones"]
        ```
    * Lists can be indexed in Groovy via numeric indices in square bracket notation:
        ```Groovy
            assert crew[0] == "Kirk"
            assert crew[0..1] == ["Kirk", "Spock"]
            assert crew[1..-1] == ["Spock", "Bones"]
        ```
* Maps:
    * Creating Maps:
        ```Groovy
            def crew = [captain: "Kirk", science: "Spock", medical: "Bones"]
        ```
        * Note, Groovy map literals create instances of `LinkedHashMap` and as a result, we can assume that element order is preserved
    * Programmatically assigning keys:
        * One can use brackets to assign variables or non-string literals as map keys
        * Example:
            ```groovy
                def map = [(arr[0]): "Kirk"]
            ```
    * As with JavaScript, we can access and assign map values using either square braces or dot notation
        * Example: `crew["medical"] or crew.medical`
* Ranges:
    * Groovy supports an iterable called a Range
        * Example:
            ```groovy
                def inc = 1..5 // inclusive
                assert inc = [1, 2, 3, 4, 5]

                def exc = 1..<5 // exclusive
                assert ecx = [1, 2, 3, 4]
            ```
    * Ranges are frequently used for slicing up strings and lists:
        * Example:
            ```groovy
                def name = "James T Kirk"
                assert name[0..4] == "James"
                assert name[0..<5] == "James"
                assert name[8..-1] == "Kirk
            ```

<br>

## Immutability
* `asImmutable()` can be called on a list or map declaration, making it immutable

<br>

## Maps As Named Arguments
* A Groovy class without an explicity constructor is given a default constructor that accepts a `Map<String, ?>`
* It will assign object properties to values found in the map
* Ex: `def person = new Person(firstName: "James", lastName: "Smith")`
    * Note: Personally, this looks to be an '~exploded' map, where the surrounding braces are omitted

<br>

## Set & Array Literals
* Groovy does not support set or array literals
* Instead, one must convert a list to a set or an array using Groovy's safe cast
* Example:
    ```Groovy
        def crewSet = ["Kirk", "Spock", "Bones"] as Set
        assert crewSet instanceof HashSet

        def crewArray = ["Kirk", "Spock", "Bones"] as String[]
        assert crewArray instanceof String[]
    ```

<br>

## Truthiness
* Any value in Groovy can be coerced to a Boolean
* Note: Groovy truthiness can be customised by implementing `asBoolean()`
    * Although this is not recommended

<br>

## Properties
* ...