package com.jrsmiffy.spock.squawker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor @Getter @Setter
public class User {
    private String name;
    private Set<User> following;
}
