package nl.crowndov.displaydirect.common.stats.domain;

import java.time.LocalDateTime;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Metric {

    private final LocalDateTime created;
    private final String name;
    private final long value;

    public Metric(String name, long value) {
        this.name = name;
        this.value = value;
        this.created = LocalDateTime.now();
    }

    public Metric(String name, long value, LocalDateTime created) {
        this.name = name;
        this.value = value;
        this.created = created;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }
}
