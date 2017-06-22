package nl.crowndov.displaydirect.commonclient.store;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DataStore {

    private static Map<Integer, PassTime> departures = new HashMap<>();

    public static void apply(PassTime p) {
        if (p.getStatus() == DisplayDirectMessage.PassingTimes.TripStopStatus.DELETED) {
            departures.remove(p.getPassTimeHash());
        } else {
            departures.put(p.getPassTimeHash(), p);
        }
    }

    public static void apply(List<PassTime> p) {
        p.forEach(DataStore::apply);
    }

    public static List<PassTime> getDepartures() {
        return departures.values().stream()
                .filter(PassTime::isCurrent)
                .sorted(Comparator.comparingInt(PassTime::getExpectedDepartureTime))
                .limit(11)
                .collect(Collectors.toList());
    }


    public static Collection<PassTime> getAll() {
        return departures.values();
    }
}
