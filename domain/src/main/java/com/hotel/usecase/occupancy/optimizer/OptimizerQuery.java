package com.hotel.usecase.occupancy.optimizer;


import lombok.Value;

@Value
class OptimizerQuery {

  int premiumRoomsCount;
  int economyRoomsCount;
}
