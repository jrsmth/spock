/**
 * Integration tests for Data Ingestion
 *
 * @author J. R. Smith
 * @since 23rd August 2023
 */
@AutoConfigureMockMvc
@ActiveProfiles(['spring-test'])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DataIngestionTest extends Specification {

    @Autowired AuthTokenManager authTokenManager
    @Autowired MockMvc mockMvc
    @Autowired ApplicationContext context

    MockRestServiceServer mockServer

    def setup() {
        mockServer = MockRestServiceServer.bindTo(RestTemplateBuilder.default).ignoreExpectOrder(true).build()
    }

    @PendingFeature
    def 'should ingest document and do something with it...'() {
        given:
        def fileName = 'data-ingestion-test.pdf'
        def pdf = new File('src/test/resources/' + fileName)
        def file = new MockMultipartFile(fileName, pdf.getBytes())

        def ingestionUrl = '/app/ingestion/start'
        def token = 'Bearer ' + authTokenManager.generateToken()

        when:
        def result = mockMvc.perform(
                multipart(ingestionUrl)
                        .file(file)
                        .header('authToken', token))
                .andDo(print())

        then:
        result.andExpect(status().isOk()).andReturn()
        noExceptionThrown()
        // TODO :: Finish me off...

    }

}