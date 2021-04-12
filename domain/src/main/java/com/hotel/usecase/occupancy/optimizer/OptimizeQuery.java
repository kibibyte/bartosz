package com.hotel.usecase.occupancy.optimizer;


import lombok.Value;

@Value
class OptimizeQuery {

  Integer premiumRoomsAvailable;
  Integer economyRoomsAvailable;
}
