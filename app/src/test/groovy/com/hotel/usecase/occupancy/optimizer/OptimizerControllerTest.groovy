package com.hotel.usecase.occupancy.optimizer

import com.hotel.MockMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.mock.web.MockHttpServletResponse

import static OptimizerResult.Status
import static org.springframework.http.HttpStatus.OK

class OptimizerControllerTest extends MockMvcTest {

    private OptimizerService optimizerService = Mock()
    private JacksonTester<OptimizerRequest> requestTester
    private JacksonTester<OptimizerResult> responseTester

    def setup() {
        setupMvc(new OptimizerController(optimizerService))
    }

    def "Should return room occupancy result"() {
        given:
        def request = new OptimizerRequest(1, 1)
        def expectedResult = optimizedResult()
        optimizerService.optimize(_ as OptimizerQuery) >> expectedResult

        when:
        def response = doPost(request)

        then:
        def responseObject = responseTester.parseObject(response.contentAsString)
        response.getStatus() == OK.value()
        responseObject == expectedResult
    }

    private static OptimizerResult optimizedResult() {
        new OptimizerResult(new Status(3, 738), new Status(3, 167))
    }

    private MockHttpServletResponse doPost(OptimizerRequest request) {
        def content = requestTester.write(request).getJson()

        post("/occupancy/optimizer", content)
    }
}
