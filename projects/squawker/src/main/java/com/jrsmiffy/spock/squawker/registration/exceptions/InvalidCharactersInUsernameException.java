package com.jrsmiffy.spock.squawker.registration.exceptions;

public class InvalidCharactersInUsernameException extends RegistrationException {

    public InvalidCharactersInUsernameException() {
        super("Only alphanumeric characters and underscores are allowed in a username");
    }

}
