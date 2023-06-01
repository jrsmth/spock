package com.jrsmiffy.spock.squawker.registration.exceptions;

public class MissingUsernameException extends RegistrationException {

    public MissingUsernameException() {
        super("Username must not be null or blank");
    }

}
