package com.jrsmiffy.spock.squawker.jdbi;

import com.jrsmiffy.spock.squawker.User;

public class UserStore {

    public User find(String username) {
        return new User(username);
    }

    public User insert(String username) {
        return new User(username);
    }

}
