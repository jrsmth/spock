package com.jrsmiffy.spock.squawker.registration;

import com.google.common.base.Strings;
import com.jrsmiffy.spock.squawker.User;
import com.jrsmiffy.spock.squawker.jdbi.DataStore;
import com.jrsmiffy.spock.squawker.registration.exceptions.InvalidCharactersInUsernameException;
import com.jrsmiffy.spock.squawker.registration.exceptions.MissingUsernameException;
import com.jrsmiffy.spock.squawker.registration.exceptions.UsernameAlreadyInUseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class RegistrationServiceImpl {

    private final DataStore dataStore;

    public User register(String username) {
        if (Strings.isNullOrEmpty(username)) {
            throw new MissingUsernameException();
        }
        if (!username.matches("[a-zA-Z0-9_]+")) {
            throw new InvalidCharactersInUsernameException();
        }
        if (dataStore.usernameInUse(username)) {
            throw new UsernameAlreadyInUseException(username);
        }

        User newUser = new User(username);
        dataStore.insert(newUser);

        return newUser;
    }

}
