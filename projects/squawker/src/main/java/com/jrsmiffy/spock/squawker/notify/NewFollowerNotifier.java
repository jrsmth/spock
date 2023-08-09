package com.jrsmiffy.spock.squawker.notify;

import com.jrsmiffy.spock.squawker.notify.email.EmailMessage;
import com.jrsmiffy.spock.squawker.notify.email.EmailSender;

public class NewFollowerNotifier {

    private EmailSender emailSender;

    //@Subscribe
    public void onNewFollower(NewFollowerEvent event) {
        EmailMessage message = new EmailMessage(
                "admin@squawker.io",
                "You have a new follower!",
                "new-follower",
                event.getNewFollower().getUsername()
        );
        emailSender.send(event.getUser(), message);
    }

    public void setEmailSender(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

}
