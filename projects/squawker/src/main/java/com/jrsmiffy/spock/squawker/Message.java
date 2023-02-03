package com.jrsmiffy.spock.squawker;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor @Getter
public class Message {
    private final User postedBy;
    private final String content;
    private final Instant postedAt;

    // TODO: add the methods and TDD...
}
