package com.hotel.usecase.occupancy;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.IntStream;

public class InMemoryProspectGuestRepository implements ProspectGuestRepository {

  @Override
  public List<ProspectGuest> findAll() {
    int[] guests = {23, 45, 155, 374, 22, 99, 100, 101, 115, 209};

    return IntStream.range(0, guests.length)
        .mapToObj(i -> new ProspectGuest(i, guests[i]))
        .collect(toList());
  }
}
