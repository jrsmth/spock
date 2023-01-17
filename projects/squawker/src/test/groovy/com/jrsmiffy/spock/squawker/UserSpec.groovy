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
        expect: // NOTE: here we verify the pre-conditions of this test
        !user.follows(other)

        when:
        user.follow(other)

        then:
        user.follows(other)
    }

}
