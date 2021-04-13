package com.hotel.usecase.occupancy.optimizer;

import lombok.Value;

@Value
class OptimizerResult {

  Status premium;
  Status economy;

  @Value
  static class Status {

    int roomsOccupiedCount;
    int profit;
  }
}
