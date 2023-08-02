package com.jrsmiffy.spock.squawker.fixtures

import com.jrsmiffy.spock.squawker.Message
import com.jrsmiffy.spock.squawker.User
import com.jrsmiffy.spock.squawker.jdbi.FollowingStore
import com.jrsmiffy.spock.squawker.jdbi.MessageStore
import com.jrsmiffy.spock.squawker.jdbi.UserStore
import groovy.transform.TupleConstructor

import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Delegate to handle general fixtures logic
 *
 * @author J. R. Smith
 * @since 2nd August 2023
 */
@TupleConstructor
class FixturesDelegate {

    final MessageStore messageStore
    final UserStore userStore
    final FollowingStore followingStore
    final User user

    void postMessageBy(User poster) {
        messageStore.insert(poster, 'aaaa', Instant.now())
    }

    void postMessageBy(String posterName) {
        postMessageBy(userStore.find(posterName))
    }

    User followNewUser(String username) {
        def newUser = newUser(username)
        followingStore.follow(user, newUser)
        return newUser
    }

    void followExistingUser(String username) {
        def userToFollow = userStore.find(username)
        if (!userToFollow) throw new IllegalStateException('No such user $username')
        followingStore.follow(user, userToFollow)
    }

    User newUser(String username) {
        userStore.insert(username)
    }

    List<Message> getTimeline() {
        messageStore.timeline(user)
    }

}
