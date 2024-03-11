@Slf4j
@AutoConfigureMockMvc
@ActiveProfiles("spring-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebSocketSpec extends Specification implements WebSocketFixture {

    @LocalServerPort Integer port

    @Autowired AuthTokenManager authTokenManager
    @Autowired MockMvc mockMvc
    @Autowired ApplicationContext context
    @Autowired SimpMessagingTemplate messagingTemplate

    def topic = "/topic"
    def documentId = "001"
    def mapper = new ObjectMapper()

    String bearerToken
    BlockingQueue<Message> receivedMessages
    StompSession session

    def setup() {
        bearerToken = authTokenManager.generateToken()
        receivedMessages = new LinkedBlockingDeque<>()
        session = buildSession(port, documentId, bearerToken, topic, receivedMessages)

        await().until(this::isSubscribed)
    }

    def "should publish message when status update is made for a given id"() {
        given:
        def message = Message.builder().text("status").build()

        when:
        def result = mockMvc
                .perform(
                        post("/app/update-status/${documentId}")
                                .contentType("application/json;charset=UTF-8")
                                .header("authToken", "Bearer ${bearerToken}")
                                .content(mapper.writeValueAsString(message))
                ).andDo(print())

        then:
        result.andExpect(status().isOk())
        noExceptionThrown()

        and:
        message == receivedMessages.poll(5, SECONDS)
    }

    /** Confirms that client has subscribed to session */
    private boolean isSubscribed() {
        def message = Message.builder()
                .text(UUID.randomUUID().toString())
                .build()

        messagingTemplate.convertAndSendToUser(documentId, topic, message)

        Message response = null
        try {
            response = receivedMessages.poll(20, MILLISECONDS)

            // empty message queue before returning true
            while(response != null && message != response) {
                response = receivedMessages.poll(20, MILLISECONDS)
            }

        } catch (InterruptedException e) {
            log.debug("[isSubscribed] Received messages polling has been interrupted", e)
        }

        return response != null
    }

}