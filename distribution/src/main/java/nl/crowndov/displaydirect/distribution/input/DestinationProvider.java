package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.Destination;
import nl.crowndov.displaydirect.distribution.util.AbstractService;
import nl.crowndov.displaydirect.distribution.util.Store;

import java.util.List;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DestinationProvider extends AbstractService {

    private static final Store.DestinationStore destinations = new Store.DestinationStore();

    public static void start() {
        readFile("destinations", Store.DestinationStore.class).ifPresent(destinations::putAll);
    }

    public static void stop() {
       backup();
    }

    public static void backup() {
        writeFile("destinations", destinations);
    }

    public static void updateDestinations(List<Destination> dests) {
        dests.forEach(d -> destinations.putIfAbsent(d.getCode(), d));
    }

    public static Destination get(String code) {
        return destinations.get(code);
    }

    public static Destination get(String dataowner, String destinationCode) {
        return get(dataowner+":"+destinationCode);
    }

    public static Map<String, Destination> getAll() {
        return destinations;
    }



}
