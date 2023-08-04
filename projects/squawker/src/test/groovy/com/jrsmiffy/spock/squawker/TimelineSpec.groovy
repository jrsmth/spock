package com.jrsmiffy.spock.squawker

import com.jrsmiffy.spock.squawker.fixtures.FixturesDelegate
import com.jrsmiffy.spock.squawker.jdbi.FollowingStore
import com.jrsmiffy.spock.squawker.jdbi.MessageStore
import com.jrsmiffy.spock.squawker.jdbi.UserStore
import spock.lang.Specification
import spock.lang.Subject

import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

import static java.time.Instant.now

class TimelineSpec extends Specification {

    @Delegate
    FixturesDelegate fixtures
    def userStore = new UserStore()
    def messageStore = new MessageStore()
    def followingStore = new FollowingStore()
    // Note :: this^ is for demo-purposes and is not the ideal way of using a @Delegate

    @Subject user = new User('khan')
    // Note: @Subject is a visual-aid which denotes the field that represents our unit-under-test

    def followedUser = new User('kirk')
    def otherUser = new User('spock')

    def setup() {
        user.follow(followedUser)

        def now = now()
        postMessage(otherUser, now.minus(6, ChronoUnit.MINUTES), 'His pattern indicates two-dimensional thinking.')
        postMessage(user, now.minus(5, ChronoUnit.MINUTES), '@kirk You\'re still alive, my old friend?')
        postMessage(followedUser, now.minus(4, ChronoUnit.MINUTES), '@khan KHAAANNNN!')
        postMessage(followedUser, now.minus(3, ChronoUnit.MINUTES), '@scotty I need warp speed in three minutes or we\'re all dead!')
        postMessage(otherUser, now.minus(2, ChronoUnit.MINUTES), '@bones I\'m sorry, Doctor, I have no time to explain this logically.')
        postMessage(user, now.minus(1, ChronoUnit.MINUTES), 'It is very cold in space!')

        fixtures = new FixturesDelegate(
                messageStore,
                userStore,
                followingStore,
                user
        )

    }

    def 'a user\'s timeline contains posts from themselves and followed users'() {
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

    def 'a user\'s timeline is ordered most recent first'() {
        expect:
        with(user.timeline()) {
            postedAt == postedAt.sort().reverse()
        }
    }

    def 'a timeline cannot be modified directly'() {
        when:
        user.timeline() << new Message(user, '@kirk You\'re still alive, my old friend?', now())

        then:
        thrown(UnsupportedOperationException)
    }

    def 'when new messages are posted they appear in the timeline'() {
        given:
        def followedUser = followNewUser('kirk')

        and:
        postMessageBy(followedUser)

        when:
        postMessageBy(followedUser)

        then:
        timeline.size() == old(timeline.size()) // + 1
        // Note :: example use of `old()` to compare before and after values

        and:
        timeline.first().postedAt > old(timeline.first().postedAt)
    }

    def 'a user who does not follow anyone sees only their own messages'() {
        given:
        def user = newUser(username)
        3.times { postMessageBy(user) }

        and: // Note :: use of `and:` in the set-up to separate preconditions
        def other = newUser(otherUsername)
        3.times { postMessageBy(other) }

        expect:
        user.timeline().postedBy.every {
            it == user
        }

        where:
        username = 'spock'
        otherUsername = 'kirk'
    }

    def 'a user cannot follow someone they already follow'() {
        given:
        def user = userStore.insert(username)
        def other = userStore.insert(otherUsername)

        and: // Note :: use of `and:` in the set-up to separate preconditions
        followingStore.follow(user, other)

        when:
        followingStore.follow(user, other)

        then:
//        thrown UnableToExecuteStatementException
        noExceptionThrown()

        where:
        username = 'spock'
        otherUsername = 'kirk'
    }

    /*
    @Unroll('a message posted by #postedBy #behavior in #whose timeline')
    def 'a user only sees messages from users they follow in their timeline'() {
        given:
        dataStore.insert(message)

        expect:
        user.timeline().contains(message) == shouldAppearInTimeline

        where:
        postedBy        | shouldAppearInTimeline
        user            | true
        followedUser    | true
        notFollowedUser | false

        message = new Message(postedBy, 'Lorem ipsum dolor sit amet')
        behavior = shouldAppearInTimeline ? 'appears' : 'does not appear'
        whose = postedBy == user ? 'their own' : '$user\'s'
        // Note :: 'whose' exists solely as an explanatory variable in the @Unroll test report
    }
    */

    private static void postMessage(User poster, Instant at, String text) {
        def clock = Clock.fixed(at, ZoneOffset.UTC)
        poster.post(text, clock.instant())
    } // Question :: Is it worth putting all private helper methods in Traits so they are separate from feature methods?

}
