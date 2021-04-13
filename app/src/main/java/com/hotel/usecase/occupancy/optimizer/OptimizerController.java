package com.hotel.usecase.occupancy.optimizer;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
class OptimizerController {

  private final OptimizerService optimizerService;

  @PostMapping("/occupancy/optimizer")
  @ResponseStatus(OK)
  OptimizerResult optimize(@RequestBody @Valid OptimizerRequest req) {
    final var query = new OptimizerQuery(req.getPremiumRoomsCount(), req.getEconomyRoomsCount());

    return optimizerService.optimize(query);
  }

}
