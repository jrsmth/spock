package com.jrsmiffy.spock.squawker.notify.email;

import com.jrsmiffy.spock.squawker.User;

public interface EmailSender {
    void send(User to, EmailMessage message);
}
