package com.jrsmiffy.spock.squawker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor @Getter @Setter
public class User {
    private final String name;
    private Set<User> following = new HashSet<>();
    private final List<Message> posts = new ArrayList<>();

    public void follow(User follower) {
        this.following.add(follower);
    }

    public boolean follows(User user) {
        return following.contains(user);
    }

    public Message post(String content, Instant postedAt) {
        Message message = new Message(this, content, postedAt);
        posts.add(0, message);

        return message;
    }

    @Override
    public String toString() {
        return "@" + this.name;
    } // Note: we can implement .toString() to improve the diagnostics of the power assert

}
