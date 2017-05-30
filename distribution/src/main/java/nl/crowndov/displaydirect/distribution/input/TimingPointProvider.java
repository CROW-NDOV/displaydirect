package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.distribution.util.AbstractService;
import nl.crowndov.displaydirect.distribution.util.Store;

import java.util.Map;
import java.util.Optional;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TimingPointProvider extends AbstractService {

    private static final Store.TimingPointStore points = new Store.TimingPointStore();

    public static void start() {
        readFile("timing_points", Store.TimingPointStore.class).ifPresent(points::putAll);
    }

    public static void stop() {
        backup();
    }

    public static void backup() {
        writeFile("timing_points", points);
    }

    public static void updateTimingPoints(Map<String, String> input) {
       points.putAll(input);
    }

    public static Optional<String> getStopFromTimingPoint(String dataOwnerCode, String timingPointCode) {
        return Optional.ofNullable(points.getOrDefault(dataOwnerCode+"|"+timingPointCode, null));
    }

    public static Store.TimingPointStore getAll() {
        return points;
    }
}
