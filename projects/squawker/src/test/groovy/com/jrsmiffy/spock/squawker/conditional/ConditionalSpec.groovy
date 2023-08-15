package com.jrsmiffy.spock.squawker.conditional

import groovy.transform.Memoized
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification

class ConditionalSpec extends Specification {

    @Memoized // Note :: here we cache the result of the URL availability check (mitigate performance issues)
    static boolean available(String url) {
        try {
            url.toURL().openConnection().with {
                connectTimeout = 1000
                connect()
            }
            true
        } catch (IOException ignored) {
            false
        }
    }

    @Requires({
        available('https://spockframework.org/')
    })
    def 'a test that requires an internet connection'() {
        expect:
        1 == 1
    }

    // env.SKIP_INTEGRATION_TESTS == 'yes'
    @IgnoreIf({
        1 == 1
    })
    def 'a slow test'() {
        expect:
        1 == 1
    }

}
