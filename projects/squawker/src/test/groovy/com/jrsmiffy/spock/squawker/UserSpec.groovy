package com.jrsmiffy.spock.squawker

import spock.lang.Specification

/** Test for {@link User} */
class UserSpec extends Specification {

    def user = new User('kirk')
    def other = new User('spock')

    def "a user can follow another user"() {
        when:
        user.follow(other)

        then:
        user.getFollowing().size() == 1
        user.getFollowing().contains(other)
    }

    def "a user reports if they are following someone"() {
        expect: 'that the user is initially not followed' // NOTE: Block description example
        !user.follows(other)
        // NOTE: here we verify the pre-conditions of this test

        when:
        user.follow(other)

        then:
        user.follows(other)
    }

}
