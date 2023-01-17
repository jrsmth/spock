package com.jrsmiffy.spock.squawker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor @Getter @Setter
public class User {
    private final String name;
    private Set<User> following = new HashSet<>();

    public void follow(User follower) {
        this.following.add(follower);
    }

    public boolean follows(User user) {
        return following.contains(user);
    }

}
