package com.jrsmiffy.spock.squawker.notify

import com.jrsmiffy.spock.squawker.User
import com.jrsmiffy.spock.squawker.notify.email.EmailMessage
import com.jrsmiffy.spock.squawker.notify.email.EmailSender
import spock.lang.Specification
import spock.lang.Subject

class NewFollowerNotifierSpec extends Specification {

    @Subject notifier = new NewFollowerNotifier()

    def 'sends email to user when someone follows them'() {
        given:
        def emailSender = Mock(EmailSender)
        notifier.emailSender = emailSender

        and:
        EmailMessage message

        when:
        notifier.onNewFollower(event)

        then: // Note :: example use of parameter/argument capture
        1 * emailSender.send(user1, _) >> {
            message = it[1]
        }

        and:
        message.from == 'admin@squawker.io'
        message.subject == 'You have a new follower!'
        message.template == 'new-follower'
        message.follower == user2.username

        where:
        user1 = new User('spock')
        user2 = new User('kirk')
        event = new NewFollowerEvent(user1, user2)
    }

}
