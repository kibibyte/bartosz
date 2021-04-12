package com.hotel.usecase.occupancy.optimizer;

import lombok.Value;

@Value
class OptimizedResult {

  Status premium;
  Status economy;

  @Value
  static class Status {

    Integer roomsOccupied;
    Integer profit;
  }
}
