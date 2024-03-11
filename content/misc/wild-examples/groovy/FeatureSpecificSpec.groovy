package com.ed.traded.risk

import com.ed.configuration.model.MarketConfiguration
import com.ed.configuration.model.RiskConfiguration
import com.ed.traded.common.model.AppUser
import com.ed.traded.model.reference.Group
import com.ed.traded.model.risk.Layer
import com.ed.traded.model.risk.Risk
import com.ed.traded.model.risk.Section
import com.ed.traded.model.risk.TradeMarket
import com.ed.traded.repository.risk.RiskQuestionRepository
import com.ed.traded.repository.risk.RiskRepository
import com.ed.traded.risk.clone.RiskFamilyService
import com.ed.traded.risk.search.util.ConvertRiskUtils
import com.ed.traded.util.workflow.WorkflowHelper
import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.test.util.ReflectionTestUtils.setField

/**
 * Unit tests for the feature of risk retrieval
 *
 * @author J. R. Smith
 * @since 8th August 2023
 */

class GetRiskSpec extends Specification {

    def riskId = 1L
    def groupId = 1L
    def appUser = Stub(AppUser)

    def riskRepository = Stub(RiskRepository)
    def workflowHelper = Stub(WorkflowHelper)
    def convertRiskUtils = Stub(ConvertRiskUtils)
    def riskFamilyService = Stub(RiskFamilyService)

    def riskConfig = new RiskConfiguration(inbox: new RiskConfiguration.Inbox(5, '5'))
    def marketConfig = new MarketConfiguration(defaultNames: Map.of('layer', 'Primary', 'layer-clone', 'Layer Name'))

    def questionRepository = Mock(RiskQuestionRepository)

    @Subject
    def service = new RiskServiceImpl()

    def setup() {
        setField(service, 'riskRepository', riskRepository)
        setField(service, 'workflowHelper', workflowHelper)
        setField(service, 'convertRiskUtils', convertRiskUtils)
        setField(service, 'riskFamilyService', riskFamilyService)

        setField(service, 'riskConfig', riskConfig)
        setField(service, 'marketConfig', marketConfig)

        setField(service, 'riskQuestionRepository', questionRepository)
    }

    def 'when using carrier portal then retrieve risk with questions for this carrier only'() {
        given:
        stubGenericRiskRetrieval()

        when:
        service.getRiskWithMessagesForUser(riskId, appUser, true)

        then:
        1 * questionRepository.findAllPublicOrPrivateBelongingToUserFromRisk(*_)

        and:
        noExceptionThrown()
    }

    def 'when using trade app then retrieve risk with questions for all carriers'() {
        given:
        stubGenericRiskRetrieval()

        when:
        service.getRiskWithMessagesForUser(riskId, appUser, false)

        then:
        1 * questionRepository.findAllRiskQuestions(*_)

        and:
        noExceptionThrown()
    }

    private void stubGenericRiskRetrieval() {
        appUser.getGroupId() >> groupId

        def risk = new Risk(id: riskId, layers: [
                new Layer(id: 1L, sections: [
                        new Section(markets: [
                                new TradeMarket(group: new Group(id: groupId))
                        ])
                ])
        ])

        riskRepository.findById(_ as Long) >> Optional.of(risk)
        workflowHelper.isLineslipWorkflowEnabled(_ as Risk) >> false
        riskFamilyService.findRiskFamilyOverviewMap(*_) >> Map.of()
    }

}

// Paul’s Method of using where: & @Unroll - collapsing both features into one (I suppose this is actually neater…)
@Unroll('when using [#test] then retrieve risk with questions for [#carrierGroup]')
def 'find risk carrier questions'() {
    given:
    stubGenericRiskRetrieval()

    when:
    service.getRiskWithMessagesForUser(riskId, appUser, isCarrierRequest)

    then:
    if (isCarrierRequest) {
        1 * questionRepository.findAllPublicOrPrivateBelongingToUserFromRisk(*_)
    } else {
        1 * questionRepository.findAllRiskQuestions(*_)
    }

    and:
    noExceptionThrown()

    where:
    test            | carrierGroup          | isCarrierRequest
    'carrier portal'| 'this carrier only'   | true
    'trade app'     | 'all carriers'        | false
}