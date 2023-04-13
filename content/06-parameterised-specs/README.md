# Parameterised Specifications

<br>

## Overview
* (Reminder) Ad Nauseam:
    * A Specification class is a description of the behaviour of (system-under-test) class
        * It serves as documentation for what the code should do and actually does (providing tests pass)
* Parameterisation:
    * If multiple feature methods are describing the same general behaviour, we may be able to extract a generalised test
        * Such a generalised test could take parameters that serve each of the previously separated test cases
    * Rationale:
        * We can reduce duplicated logic across the test set up and behaviour exercise
            * This makes our tests more readable but also means that if requirements change, we only need to update the single test
    * GRoT:
        * We should look to parameterisation where possible:
            * To reduce code duplication but also to encourage us to extend the coverage of our feature methods (by considering what happens when we leave the happy path)
