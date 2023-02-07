package com.jrsmiffy.spock.squawker;

import lombok.Getter;

import java.time.Instant;

@Getter
public class Message {
    public static final int MAX_TEXT_LENGTH = 140;

    private final User postedBy;
    private final String text;
    private final Instant postedAt;

    public Message(User postedBy, String text, Instant postedAt) {
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException("Message text cannot be longer than 140 characters");
        }

        this.postedBy = postedBy;
        this.text = text;
        this.postedAt = postedAt;
    }

    // TODO: add the methods and TDD...
}
