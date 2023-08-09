package com.jrsmiffy.spock.squawker.notify;

import com.jrsmiffy.spock.squawker.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class NewFollowerEvent {

    private final User user;
    private final User newFollower;

}
