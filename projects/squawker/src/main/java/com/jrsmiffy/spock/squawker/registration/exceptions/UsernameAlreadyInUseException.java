package com.jrsmiffy.spock.squawker.registration.exceptions;

public class UsernameAlreadyInUseException extends RegistrationException {

    public UsernameAlreadyInUseException(String username) {
        super(String.format("The username '%s' is already in use", username));
    }

}
