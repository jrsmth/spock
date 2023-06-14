package com.jrsmiffy.spock.squawker.registration

import com.jrsmiffy.spock.squawker.User
import com.jrsmiffy.spock.squawker.jdbi.DataStore
import com.jrsmiffy.spock.squawker.registration.exceptions.InvalidCharactersInUsernameException
import com.jrsmiffy.spock.squawker.registration.exceptions.MissingUsernameException;
import com.jrsmiffy.spock.squawker.registration.exceptions.RegistrationException
import com.jrsmiffy.spock.squawker.registration.exceptions.UsernameAlreadyInUseException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

/**
 * Unit tests for {@link RegistrationServiceImpl}
 *
 * @author J. R. Smith
 * @since 01/06/2023
 */
class RegistrationSpec extends Specification {

    def dataStore = Mock(DataStore)
    
    @Subject
    def subject = new RegistrationServiceImpl(dataStore)

    @Shared
    def usedUsername = 'spock'

    def 'a new user cannot register with an invalid username'() {
        given:
        dataStore.usernameInUse('spock') >> true

        when:
        subject.register(username)

        then:
        thrown(RegistrationException)

        and:
        0 * dataStore.insert(_ as User)

        where: // Note :: the where: block indicates this feature method is parameterised and will run once for each data point
        username << [null, '', '     ', '@&%\$+[', 'spock']
    }

    @Unroll
    def 'a new user cannot register with the username `#username`'() {
        given:
        dataStore.usernameInUse(_ as String) >> true

        when:
        subject.register(username)

        then:
        thrown(exceptionType)

        and:
        0 * dataStore.insert(_ as User)

        where:
        username                   | exceptionType
        null                       | MissingUsernameException
        ''                         | MissingUsernameException
        '     '                    | InvalidCharactersInUsernameException
        '@&%\$+['                  | InvalidCharactersInUsernameException
        usedUsername               | UsernameAlreadyInUseException
        usedUsername.toLowerCase() | UsernameAlreadyInUseException
        usedUsername.toUpperCase() | UsernameAlreadyInUseException

    }
    
}
