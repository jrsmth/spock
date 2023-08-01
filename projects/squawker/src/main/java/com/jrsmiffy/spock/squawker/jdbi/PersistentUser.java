package com.jrsmiffy.spock.squawker.jdbi;

import com.jrsmiffy.spock.squawker.Message;
import com.jrsmiffy.spock.squawker.User;

import java.time.Instant;

/**
 * Persistent User
 *
 * @author J. R. Smith
 * @since 1st August 2023
 */
class PersistentUser extends User {

    private final DataStore dataStore;

    PersistentUser(DataStore dataStore, String username) {
        super(username);
        this.dataStore = dataStore;
    }

    @Override
    public Message post(String messageText, Instant postedAt) {
        Message message = new Message(this, messageText, postedAt);
        dataStore.insert(message);

        return message;
    }

}
