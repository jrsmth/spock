package com.jrsmiffy.spock.squawker

import spock.lang.Ignore
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
        user.posts.text == [message]
    }

    def 'a user\'s posts are listed with the most recent first'() {
        when:
        user.post("It's life, Jim", now())
        user.post("but not as we know it", now())

        then:
        user.posts*.text == ["but not as we know it", "It's life, Jim"]
        // Note: here we are using the spread operator (*.) [but it seems redundant!]
    }

    def 'the posting user is recorded in the message'() {
        when:
        user.post('Fascinating', now())
        user.post('@bones I was merely stating a fact, Doctor', now())

        then:
        user.posts.every {
            it.postedBy == user
        }
        // Note: here we are using .every() to loop over assertions
    }

    @Ignore
    def 'the list of posts is not modifiable'() {
        when:
        user.posts << new Message(user, 'Fascinating!', now())

        then:
        thrown(UnsupportedOperationException)
    }

    def 'a posted message may not be longer than 140 characters'() {
        given:
        def messageText = """Lieutenant, I am half Vulcanian. Vulcanians do not
        speculate. I speak from pure logic. If I let go of a hammer on a planet
        that has a positive gravity, I need not see it fall to know that it has in
        fact fallen."""

        expect:
        messageText.length() > Message.MAX_TEXT_LENGTH

        when:
        user.post(messageText, now())

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Message text cannot be longer than 140 characters'
        // Note: here we are further interrogating the exception
    }

}
