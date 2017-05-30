package nl.crowndov.displaydirect.dashboard.mqtt;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.dashboard.logs.LogStore;
import nl.crowndov.displaydirect.dashboard.messages.DisplayDirectMessageParser;
import nl.crowndov.displaydirect.dashboard.metrics.MetricStore;
import org.fusesource.mqtt.client.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MonitoringHandler implements Callback<DisplayDirectMessage.Monitoring> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringHandler.class);

    @Override
    public void onSuccess(DisplayDirectMessage.Monitoring value) {
        if (value.getLogsList().size() > 0) {
            value.getLogsList().forEach(m -> LogStore.add(DisplayDirectMessageParser.toLogMessage(m)));
        } else if (value.getMetricsList().size() > 0) {
            value.getMetricsList().forEach(m -> MetricStore.update(DisplayDirectMessageParser.toMetric(m)));
        } else {
            LOGGER.error("Got empty log message");
        }
    }

    @Override
    public void onFailure(Throwable value) {
        LOGGER.info("Failed with error", value);
    }
}
