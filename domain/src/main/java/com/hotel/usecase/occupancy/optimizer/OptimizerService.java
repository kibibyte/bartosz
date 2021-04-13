package com.hotel.usecase.occupancy.optimizer;

import static java.util.Comparator.reverseOrder;

import java.util.stream.Collectors;

import com.hotel.usecase.occupancy.ProspectGuest;
import com.hotel.usecase.occupancy.ProspectGuestRepository;
import com.hotel.usecase.occupancy.optimizer.OptimizerResult.Status;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class OptimizerService {

  private final ProspectGuestRepository guestRepository;

  OptimizerResult optimize(OptimizerQuery query) {

    var guests = guestRepository.findAll();

    var premiumGuests = guests.stream()
        .map(ProspectGuest::getPriceOffered)
        .filter(t -> t >= 100)
        .sorted(reverseOrder())
        .limit(query.getPremiumRoomsCount())
        .collect(Collectors.toList());

    var economyProspects = guests.stream()
        .map(ProspectGuest::getPriceOffered)
        .filter(t -> t < 100)
        .sorted(reverseOrder())
        .collect(Collectors.toList());

    var numberOfPremiumRoomsLeft = query.getPremiumRoomsCount() - premiumGuests.size();

    if (numberOfPremiumRoomsLeft > 0 && (economyProspects.size() > query.getEconomyRoomsCount())) {
      var guestsToUpgrade = economyProspects.stream()
          .limit(numberOfPremiumRoomsLeft)
          .collect(Collectors.toList());

      premiumGuests.addAll(guestsToUpgrade);
      economyProspects.removeAll(guestsToUpgrade);
    }

    var economyGuests = economyProspects.stream()
        .limit(query.getEconomyRoomsCount())
        .collect(Collectors.toList());

    var premiumStatus = new Status(premiumGuests.size(),
        premiumGuests.stream().mapToInt(Integer::intValue).sum());

    var economyStatus = new Status(economyGuests.size(),
        economyGuests.stream().mapToInt(Integer::intValue).sum());

    return new OptimizerResult(premiumStatus, economyStatus);
  }
}
