package com.jrsmiffy.spock.squawker;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class Message implements Comparable<Message> {

    public static final int MAX_TEXT_LENGTH = 140;

    private final UUID id;
    private final User postedBy;
    private final String text;
    private final Instant postedAt;

    public Message(User postedBy, String text) {
        this(UUID.randomUUID(), postedBy, text, Instant.now());
    }

    public Message(User postedBy, String text, Instant postedAt) {
        this(UUID.randomUUID(), postedBy, text, postedAt);
    }

    public Message(UUID id, User postedBy, String text, Instant postedAt) {
        if (text.length() > MAX_TEXT_LENGTH) {
            throw new IllegalArgumentException("Message text cannot be longer than 140 characters");
        }

        this.id = id;
        this.postedBy = postedBy;
        this.text = text;
        this.postedAt = postedAt;
    }

    @Override
    public int compareTo(Message other) {
        return -postedAt.compareTo(other.postedAt);
    }

    // TODO: add the methods and TDD...
}
