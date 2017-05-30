package nl.crowndov.displaydirect.dashboard.metrics;

import nl.crowndov.displaydirect.common.stats.domain.Metric;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MetricStore {

    private static Map<String, Metric> metrics = new HashMap<>();

    public static Map<String, Metric> get() {
        return metrics;
    }

    public static void update(Metric in) {
        metrics.put(in.getName(), in);
    }


}
