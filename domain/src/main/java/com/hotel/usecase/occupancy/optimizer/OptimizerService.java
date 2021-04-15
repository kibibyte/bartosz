package com.hotel.usecase.occupancy.optimizer;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;

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

    if (canUpgradeGuests(premiumRoomsLeft, economyRoomsCount, economyGuests.size())) {
      upgradeGuests(premiumGuests, economyGuests, premiumRoomsLeft);
    }

    final var premiumStatus = status(premiumGuests, premiumRoomsCount);
    final var economyStatus = status(economyGuests, economyRoomsCount);

    return new OptimizerResult(premiumStatus, economyStatus);
  }

  private boolean canUpgradeGuests(int premiumRoomsLeft, int economyRoomsCount, int economyGuestsCount) {
    return premiumRoomsLeft > 0 && economyGuestsCount > economyRoomsCount;
  }

  private void upgradeGuests(List<ProspectGuest> premiumGuests, List<ProspectGuest> economyGuests,
      int premiumRoomsLeft) {
    final var upgradableGuests = getUpgradableGuests(economyGuests, premiumRoomsLeft);

    premiumGuests.addAll(upgradableGuests);
    economyGuests.removeAll(upgradableGuests);
  }

  private Status status(final List<ProspectGuest> guests, int roomsLimit) {
    final var guestsLimited = guests.stream()
        .limit(roomsLimit)
        .collect(toList());

    final var profit = guestsLimited.stream().mapToInt(ProspectGuest::getPriceOffered).sum();

    return new Status(guestsLimited.size(), profit);
  }

  private List<ProspectGuest> getUpgradableGuests(List<ProspectGuest> economyGuests, int premiumRoomsLeft) {
    return economyGuests.stream()
        .limit(premiumRoomsLeft)
        .collect(toList());
  }

  private List<ProspectGuest> filterGuests(List<ProspectGuest> guests, Predicate<ProspectGuest> guestPredicate) {
    return guests.stream()
        .filter(guestPredicate)
        .sorted(comparingInt(ProspectGuest::getPriceOffered).reversed())
        .collect(toList());
  }
}
