package nl.crowndov.displaydirect.distribution.messages.processing;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.messages.SubscriptionStore;
import nl.crowndov.displaydirect.distribution.stats.MetricStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class SubscriptionCheckProcessor implements Processor<PassTime> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionCheckProcessor.class);

    private static MetricStore metrics = MetricStore.getInstance();

    @Override
    public PassTime process(PassTime input) {
        if (input.getQuayCode() != null && SubscriptionStore.hasSubscription(input.getQuayCode())) {
            metrics.increaseBucketValue("kv78turbo.subscriptions.transported", ChronoUnit.HOURS);
            // TODO: Store the messages anyway for calculating delta's and new subscribers
            return input;
        }

        metrics.increaseBucketValue("kv78turbo.subscriptions.ignored", ChronoUnit.HOURS);
        return null;
    }
}
