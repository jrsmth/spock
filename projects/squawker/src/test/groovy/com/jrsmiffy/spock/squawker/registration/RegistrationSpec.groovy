package com.jrsmiffy.spock.squawker.registration

import com.jrsmiffy.spock.squawker.User
import com.jrsmiffy.spock.squawker.jdbi.DataStore;
import com.jrsmiffy.spock.squawker.registration.RegistrationServiceImpl
import com.jrsmiffy.spock.squawker.registration.exceptions.RegistrationException
import spock.lang.Specification
import spock.lang.Subject

/**
 * Unit tests for {@link RegistrationServiceImpl}
 */
class RegistrationSpec extends Specification {
    
    def dataStore = Mock(DataStore)
    
    @Subject
    def subject = new RegistrationServiceImpl(dataStore)

    def 'a new user cannot register with an invalid username'() {
        given:
        dataStore.usernameInUse('spock') >> true

        when:
        subject.register(username)

        then:
        thrown(RegistrationException)

        and:
        0 * dataStore.insert(_ as User)

        where: // Note :: the where: block indicates this feature method is parameterised and will one run for each data point
        username << [null, '', '     ', '@&%\$+[', 'spock']
    }
    
}
