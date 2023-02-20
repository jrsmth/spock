# Managing Resources

<br>

## Persistence Testing
* Testing persistence is a frequent use case in integration testing
    * When performed incorrectly, it can render your test suite ineffective through slow and brittle tests
* A common anti-pattern is to test everything by reference to a single monolithic fixture
    * That contains an ever growing volume of data which attemots to cater for every use case
        * Such a fixture does not scale well
    * You want to avoid a situation where a test suite takes hours to run
        * As well as no one being sure whether or not passing tests actually means the code is working correctly
* General rules of thumb:
    * Set up the minimum amount of data required for a specific test (no excess data)
    * Keep everything in-memory AMAP (H2 by default)
* NEXT: Testing a persistence layer example... page 40...