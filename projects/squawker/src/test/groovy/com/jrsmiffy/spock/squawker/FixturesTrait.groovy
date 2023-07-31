package com.jrsmiffy.spock.squawker

import com.jrsmiffy.spock.squawker.jdbi.MessageStore

import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Trait to handle general fixtures logic
 *
 * @author J. R. Smith
 * @since 31st July 2023
 */
trait FixturesTrait {

    abstract MessageStore getMessageStore()
    abstract User getUser()
    // Note :: these abstract methods need to be implemented in the Specification that uses this trait

    void postMessageBy(User poster) {
        messageStore.insert(poster, "aaaa", Instant.now())
    }

    void postMessageAt(int minutesAgo) {
        def timestamp = Instant.now().minus(minutesAgo, ChronoUnit.MINUTES)
        messageStore.insert(user, "aaaaa", timestamp)
    }

    def followNewUser(String username) {
        def newUser = newUser(username)
        followingStore.follow(user, newUser)

        return newUser
    }

    def newUser(String username) {
        userStore.insert(username)
    }

    def getTimeline() {
        messageStore.timeline(user)
    }

}
