package com.jrsmiffy.spock.squawker

import spock.lang.Specification

import java.time.Instant

import static java.time.Instant.now

/** Test for {@link User} */
class UserSpec extends Specification {

    def user = new User('kirk')
    def other = new User('spock')

    def 'a user can follow another user'() {
        when:
        user.follow(other)

        then:
        user.getFollowing().size() == 1
        user.getFollowing().contains(other)
    }

    def 'a user reports if they are following someone'() {
        expect: 'that the user is initially not followed' // Note: block description example
        !user.follows(other)
        // Note: here we verify the pre-conditions of this test

        when:
        user.follow(other)

        then:
        user.follows(other)
    }
    
    def 'a user can post a message'() {
        given:
        def message = '@kirk that is illogical, Captain!'

        when:
        user.post(message, Instant.now())

        then:
        user.posts.content == [message]
    }

    def 'a user\'s posts are listed with the most recent first'() {
        when:
        user.post("It's life, Jim", now())
        user.post("but not as we know it", now())

        then:
        user.posts*.content == ["but not as we know it", "It's life, Jim"]
        // Note: here we are using the spread operator (*.) [but it seems redundant!]
    }

}
