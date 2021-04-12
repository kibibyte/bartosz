package com.hotel.usecase.occupancy.optimizer;

import com.hotel.usecase.occupancy.ProspectGuestRepository;
import com.hotel.usecase.occupancy.optimizer.OptimizedResult.Status;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class OptimizerService {

  private final ProspectGuestRepository guestRepository;

  OptimizedResult optimize(OptimizeQuery query) {

    var premiumStatus = new Status(1,1);
    var economyStatus = new Status(1,1);

    return new OptimizedResult(premiumStatus, economyStatus);
  }
}
