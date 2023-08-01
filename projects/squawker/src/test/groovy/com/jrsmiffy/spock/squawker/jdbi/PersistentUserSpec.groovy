package com.jrsmiffy.spock.squawker.jdbi

import spock.lang.Specification
import spock.lang.Subject

import static java.time.Instant.now

/**
 * Unit tests for {@link PersistentUser}
 * 
 * @author J. R. Smith
 * @since 1st August 2023
 */
class PersistentUserSpec extends Specification {

    def dataStore = Mock(DataStore)

    @Subject
    user = new PersistentUser(dataStore, 'spock')

    void insertsMessageOnce(String text) {
        // Note :: extraction of mock interaction using helper method
        1 * dataStore.insert({ it.text == text } )
    }

    def 'posting a message inserts it to the database'() {
        when:
        user.post(messageText, now())

        then:
        interaction { // Note :: 'interaction' is an in-built Spock method
            insertsMessageOnce(messageText)
            // Note :: Closure required for Spock to recognise mocking interaction
        }

        where:
        messageText = 'Fascinating!'
    }
    
}
