package nl.crowndov.displaydirect.distribution.messages.processing;

import nl.crowndov.displaydirect.distribution.chb.StopStore;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.stats.MetricStore;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class StopCodeProcessor implements Processor<RealtimeMessage> {

    private static MetricStore metrics = MetricStore.getInstance();

    @Override
    public RealtimeMessage process(RealtimeMessage input) {
        Optional<String> quay = StopStore.getQuayFromCode(input.getDataOwnerCode(), input.getStopCode());
        if (quay.isPresent()) {
            input.setQuayCode(quay.get());
            metrics.increaseBucketValue("kv78turbo.stops.matched", ChronoUnit.HOURS);
            return input;
        }
        metrics.increaseBucketValue("kv78turbo.stops.rejected", ChronoUnit.HOURS);
        return null;
    }
}
