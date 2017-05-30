package nl.crowndov.displaydirect.distribution.stats;


import nl.crowndov.displaydirect.common.stats.domain.Metric;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MetricStore {

    private static List<Metric> metrics = new ArrayList<>();
    private static ConcurrentMap<String, ConcurrentMap<LocalDateTime, Metric>> buckets = new ConcurrentHashMap<>();
    private static MetricStore instance;

    public static synchronized MetricStore getInstance() {
        if (instance == null) {
            instance = new MetricStore();
        }
        return instance;
    }

    public void storeMetric(String name, long value) {
        Metric m = new Metric(name, value);
        metrics.add(m);
    }

    public void setBucketValue(String name, TemporalUnit bucket, long value) {
        ensureMetricInBucket(name);
        LocalDateTime bucketSlot = LocalDateTime.now().truncatedTo(bucket);
        buckets.get(name).put(bucketSlot, new Metric(name, value, bucketSlot));
    }

    public void increaseBucketValue(String name, TemporalUnit bucket) {
        ensureMetricInBucket(name);
        LocalDateTime key = LocalDateTime.now().truncatedTo(bucket);
        if (!buckets.get(name).containsKey(key)) {
            buckets.get(name).put(key, new Metric(name, 1));
        } else {
            Metric previous = buckets.get(name).get(key);
            buckets.get(name).put(key, new Metric(name, previous.getValue() + 1, key));
        }
    }

    private void ensureMetricInBucket(String name) {
        if (!buckets.containsKey(name)) {
            buckets.put(name, new ConcurrentSkipListMap<>());
        }
    }

    public Map<String, List<Metric>> getMetrics() {
        return Stream.concat(buckets.values().stream().flatMap(m -> m.values().stream()),  metrics.stream())
                .collect(Collectors.groupingBy(Metric::getName));
    }

    public List<Metric> getMetric(String name) {
        if (buckets.containsKey(name)) {
            return new ArrayList<>(buckets.get(name).values());
        }
        return metrics.parallelStream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
}
