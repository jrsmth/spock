package com.jrsmiffy.spock.squawker

import spock.lang.Specification
import spock.lang.Subject

import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

import static java.time.Instant.now

class TimelineSpec extends Specification {

    @Subject user = new User("khan")
    // Note: @Subject is a visual-aid which denotes the field that represents our unit-under-test

    def followedUser = new User("kirk")
    def otherUser = new User("spock")

    def setup() {
        user.follow(followedUser)

        def now = now()
        postMessage(otherUser, now.minus(6, ChronoUnit.MINUTES), "His pattern indicates two-dimensional thinking.")
        postMessage(user, now.minus(5, ChronoUnit.MINUTES), "@kirk You're still alive, my old friend?")
        postMessage(followedUser, now.minus(4, ChronoUnit.MINUTES), "@khan KHAAANNNN!")
        postMessage(followedUser, now.minus(3, ChronoUnit.MINUTES), "@scotty I need warp speed in three minutes or we're all dead!")
        postMessage(otherUser, now.minus(2, ChronoUnit.MINUTES), "@bones I'm sorry, Doctor, I have no time to explain this logically.")
        postMessage(user, now.minus(1, ChronoUnit.MINUTES), "It is very cold in space!")
    }

    def "a user's timeline contains posts from themselves and followed users"() {
        expect:
        with(user.timeline()) {
            size() == 4

            it*.postedBy.every {
                it in [user, followedUser]
            }

            !it*.postedBy.any {
                it == otherUser
            }
        }
    }

    def "a user's timeline is ordered most recent first"() {
        expect:
        with(user.timeline()) {
            postedAt == postedAt.sort().reverse()
        }
    }

    def "a timeline cannot be modified directly"() {
        when:
        user.timeline() << new Message(user, "@kirk You're still alive, my old friend?", now())

        then:
        thrown(UnsupportedOperationException)
    }

    private void postMessage(User poster, Instant at, String text) {
        def clock = Clock.fixed(at, ZoneOffset.UTC)
        poster.post(text, clock.instant())
    }

}
