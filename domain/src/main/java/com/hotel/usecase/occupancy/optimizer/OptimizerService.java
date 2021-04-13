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
    final var premiumGuests = getGuests(guests, g -> g.getPriceOffered() >= PREMIUM_ROOM_PRICE);
    final var economyGuests = getGuests(guests, g -> g.getPriceOffered() < PREMIUM_ROOM_PRICE);

    final int premiumRoomsLeft = getPremiumRoomsLeft(query.getPremiumRoomsCount(), premiumGuests.size());

    if (canUpgradeGuests(premiumRoomsLeft, economyGuests.size(), query.getEconomyRoomsCount())) {
      upgradeGuests(premiumGuests, economyGuests, premiumRoomsLeft);
    }

    final var premiumStatus = getStatus(premiumGuests, query.getPremiumRoomsCount());
    final var economyStatus = getStatus(economyGuests, query.getEconomyRoomsCount());

    return new OptimizerResult(premiumStatus, economyStatus);
  }

  private void upgradeGuests(List<ProspectGuest> premiumGuests, List<ProspectGuest> economyGuests,
      int premiumRoomsLeft) {
    final var upgradableGuests = getUpgradableGuests(economyGuests, premiumRoomsLeft);

    premiumGuests.addAll(upgradableGuests);
    economyGuests.removeAll(upgradableGuests);
  }

  private boolean canUpgradeGuests(int premiumRoomsLeft, int economyGuestsCount, int economyRoomsCount) {
    return premiumRoomsLeft > 0 && (economyGuestsCount > economyRoomsCount);
  }

  private Status getStatus(final List<ProspectGuest> guests, int roomsLimit) {
    final var guestsLimited = guests.stream()
        .limit(roomsLimit)
        .collect(toList());

    return new Status(guestsLimited.size(),
        guestsLimited.stream()
            .mapToInt(ProspectGuest::getPriceOffered).sum());
  }

  private List<ProspectGuest> getUpgradableGuests(List<ProspectGuest> economyGuests, int premiumRoomsLeft) {
    return economyGuests.stream()
        .limit(premiumRoomsLeft)
        .collect(toList());
  }

  private int getPremiumRoomsLeft(int premiumRoomsCount, int premiumGuestsCount) {
    return premiumRoomsCount - premiumGuestsCount;
  }

  private List<ProspectGuest> getGuests(List<ProspectGuest> guests, Predicate<ProspectGuest> predicate) {
    return guests.stream()
        .filter(predicate)
        .sorted(comparingInt(ProspectGuest::getPriceOffered).reversed())
        .collect(toList());
  }
}
