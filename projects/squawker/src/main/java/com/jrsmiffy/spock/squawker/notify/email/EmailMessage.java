package com.jrsmiffy.spock.squawker.notify.email;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class EmailMessage {

    private final String from;
    private final String subject;
    private final String template;
    private final String follower;

}
