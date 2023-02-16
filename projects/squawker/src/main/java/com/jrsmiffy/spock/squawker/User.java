package com.jrsmiffy.spock.squawker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.time.Instant.now;

@RequiredArgsConstructor @Getter
public class User {
    private final String username;
    private final Set<User> following = new HashSet<>();
    private final List<Message> posts = new ArrayList<>();
    private final Instant registered = now();

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

    public List<Message> timeline() {
        List<Message> timeline = new ArrayList<>(posts);

        for (User user : following) timeline.addAll(user.posts);
        Collections.sort(timeline);

        return Collections.unmodifiableList(timeline);
    }

    @Override
    public String toString() {
        return "@" + this.username;
    } // Note: we can implement .toString() to improve the diagnostics of the power assert

}
