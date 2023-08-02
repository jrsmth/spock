package com.jrsmiffy.spock.squawker.jdbi;

import com.jrsmiffy.spock.squawker.Message;
import com.jrsmiffy.spock.squawker.User;

import java.time.Instant;
import java.util.List;

public class MessageStore {

    public void insert(User user, String message, Instant timestamp) {
    }

    public List<Message> timeline(User user) {
        return List.of(new Message(user, "message"));
    }

}
