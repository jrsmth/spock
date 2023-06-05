package com.jrsmiffy.spock.squawker.jdbi;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DataStore {

    public void insert(Object object) {
        log.info("Saving object [{}]", object);
    }

    public boolean usernameInUse(String username) {
        log.info("Checking if username [{}] matches the default-user", username);

        return username.equals("default-user");
    }

}
