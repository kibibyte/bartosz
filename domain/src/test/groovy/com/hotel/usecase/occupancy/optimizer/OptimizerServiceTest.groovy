package com.hotel.usecase.occupancy.optimizer

import spock.lang.Specification

import static com.hotel.usecase.occupancy.optimizer.OptimizedResult.Status

class OptimizerServiceTest extends Specification {

    def "Should optimize hotel rooms occupancy"() {
        given:
        def service = new OptimizerService()

        when:
        def actualResult = service.optimize(optimizeQuery)

        then:
        actualResult == expectedResult

        where:
        optimizeQuery           || expectedResult
        new OptimizeQuery(1, 1) || new OptimizedResult(new Status(1, 1), new Status(1, 1))
    }
}
