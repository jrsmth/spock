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
    * 