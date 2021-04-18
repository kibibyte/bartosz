package com.hotel.usecase.occupancy.optimizer;

import static java.util.Collections.emptyList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.hotel.usecase.occupancy.ProspectGuest;
import com.hotel.usecase.occupancy.ProspectGuestRepository;
import com.hotel.usecase.occupancy.optimizer.OptimizerResult.Status;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class OptimizerService {

  private final int PREMIUM_ROOM_PRICE = 100;

  private final ProspectGuestRepository guestRepository;

  OptimizerResult optimize(OptimizerQuery query) {
    final var guests = guestRepository.findAll();
    final var premiumGuests = filterGuests(guests, g -> g.getPriceOffered() >= PREMIUM_ROOM_PRICE);
    final var economyGuests = filterGuests(guests, g -> g.getPriceOffered() < PREMIUM_ROOM_PRICE);
    final var premiumRoomsCount = query.getPremiumRoomsCount();
    final int economyRoomsCount = query.getEconomyRoomsCount();
    final int premiumRoomsLeft = premiumRoomsCount - premiumGuests.size();
    final var upgradableGuests = getUpgradableGuests(economyGuests, economyRoomsCount, premiumRoomsLeft);

    final var premiumGuestsUpgraded = combine(premiumGuests, upgradableGuests)
        .limit(premiumRoomsCount)
        .collect(toList());

    final var economyGuestsUpgraded = combine(economyGuests, upgradableGuests)
        .filter(guest -> !upgradableGuests.contains(guest))
        .limit(economyRoomsCount)
        .collect(toList());

    return new OptimizerResult(
        Status.of(premiumGuestsUpgraded),
        Status.of(economyGuestsUpgraded)
    );
  }

  private Stream<ProspectGuest> combine(List<ProspectGuest> guests, List<ProspectGuest> upgradableGuests) {
    return Stream.of(guests, upgradableGuests)
        .flatMap(Collection::stream)
        .sorted(comparingInt(ProspectGuest::getPriceOffered).reversed());
  }

  private List<ProspectGuest> getUpgradableGuests(List<ProspectGuest> economyGuests, int economyRoomsCount,
      int premiumRoomsLeft) {
    if (canUpgradeGuests(premiumRoomsLeft, economyRoomsCount, economyGuests.size())) {
      return economyGuests.stream()
          .sorted(comparingInt(ProspectGuest::getPriceOffered).reversed())
          .limit(premiumRoomsLeft)
          .collect(toList());
    }

    return emptyList();
  }

  private boolean canUpgradeGuests(int premiumRoomsLeft, int economyRoomsCount, int economyGuestsCount) {
    return premiumRoomsLeft > 0 && economyGuestsCount > economyRoomsCount;
  }

  private List<ProspectGuest> filterGuests(List<ProspectGuest> guests, Predicate<ProspectGuest> guestPredicate) {
    return guests.stream()
        .filter(guestPredicate)
        .collect(toList());
  }
}