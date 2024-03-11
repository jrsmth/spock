class ArtificialWebhookServiceTest extends Specification {

    @Subject
    def underTest = new ArtificialWebhookService()

    def artificialAPIService = Mock(ArtificialAPIService)
    def apiEnabledFilter = Mock(QuoteApiPollingEnabledFilter)
    def restClient = Mock(HttpRestClient)
    def objectMapper = Spy(ObjectMapper)

    def slurper = new JsonSlurper()

    def setup() {
        ReflectionTestUtils.setField(underTest, "filterPath", '.field')
        ReflectionTestUtils.setField(underTest, "webhookBase", 'webhookBase')
        ReflectionTestUtils.setField(underTest, "apiEnabledFilter", apiEnabledFilter)
        ReflectionTestUtils.setField(underTest, "artificialAPIService", artificialAPIService)
        ReflectionTestUtils.setField(underTest, "objectMapper", objectMapper)
        ReflectionTestUtils.setField(underTest, "restClient", restClient)
    }

    def 'should contain valid filtering expression when webhook is registered'() {
        when:
        underTest.setupWebhookCallback(Stub(ExtendedQuoteAPIConfig), 'traded', '/callback', Stub(ArtificialWebhookRegistration))

        then:
        1 * restClient.put(*_) >> { arguments ->
            def subscribeMap = slurper.parseText(arguments[2]) as Map

            // match for ${.<FIELD>}
            assert subscribeMap.get("path") ==~ /\$\{\..*\}/

            return HttpResponse.builder().body(null).build()
        }

        and:
        1 * artificialAPIService.getToken(_) >> 'token'
        1 * apiEnabledFilter.isApiEnabled(*_) >> false

    }

}