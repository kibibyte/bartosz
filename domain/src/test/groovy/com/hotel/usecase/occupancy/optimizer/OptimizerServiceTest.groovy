package com.hotel.usecase.occupancy.optimizer

import com.hotel.usecase.occupancy.ProspectGuest
import com.hotel.usecase.occupancy.ProspectGuestRepository
import spock.lang.Specification

import static com.hotel.usecase.occupancy.optimizer.OptimizedResult.Status

class OptimizerServiceTest extends Specification {

    def "Should optimize hotel rooms occupancy"() {
        given:
        ProspectGuestRepository prospectRepository = Mock()
        prospectRepository.findAll() >> getProspects()

        def service = new OptimizerService(prospectRepository)

        when:
        def actualResult = service.optimize(optimizeQuery)

        then:
        actualResult == expectedResult

        where:
        optimizeQuery           || expectedResult
        new OptimizeQuery(3, 3) || new OptimizedResult(new Status(3, 738), new Status(3, 167))
        new OptimizeQuery(7, 5) || new OptimizedResult(new Status(6, 1054), new Status(4, 189))
        new OptimizeQuery(2, 7) || new OptimizedResult(new Status(2, 583), new Status(4, 189))
        new OptimizeQuery(7, 1) || new OptimizedResult(new Status(1, 1153), new Status(1, 45))
    }

    private List<ProspectGuest> getProspects() {
        [23, 45, 155, 374, 22, 99, 100, 101, 115, 209]
                .withIndex()
                .collect() { int e, int i -> new ProspectGuest(i, e) }
    }
}
