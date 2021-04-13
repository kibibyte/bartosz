package com.hotel.usecase.occupancy.optimizer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Value;

@Value
class OptimizerRequest {

  @NotNull
  @Min(value = 0)
  Integer premiumRoomsCount;

  @NotNull
  @Min(value = 0)
  Integer economyRoomsCount;
}
