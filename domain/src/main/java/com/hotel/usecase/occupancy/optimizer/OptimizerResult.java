package com.hotel.usecase.occupancy.optimizer;

import java.util.List;

import com.hotel.usecase.occupancy.ProspectGuest;

import lombok.Value;

@Value
class OptimizerResult {

  Status premium;
  Status economy;

  @Value
  static class Status {

    int roomsOccupiedCount;
    int profit;

    static Status of(List<ProspectGuest> guests) {
      final var profit = guests.stream()
          .mapToInt(ProspectGuest::getPriceOffered).sum();

      return new Status(guests.size(), profit);
    }
  }
}
