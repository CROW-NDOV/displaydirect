package nl.crowndov.displaydirect.distribution.stats;

import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.distribution.messages.DisplayDirectMessageFactory;
import nl.crowndov.displaydirect.distribution.util.CatchableRunnable;
import nl.crowndov.displaydirect.common.stats.domain.Metric;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MetricPusher  {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetricPusher.class);

    private static ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
    private static MetricStore metrics = MetricStore.getInstance();
    private static Transport transport = TransportFactory.get();

    public static void start() {
        Runnable get = new CatchableRunnable(MetricPusher::sendMetrics);
        schedule.scheduleAtFixedRate(get, 0, 20, TimeUnit.SECONDS);
    }

    private static void sendMetrics() {
        LOGGER.trace("Sending metrics");
        // Find the latest values
        List<Metric> m = metrics.getMetrics().entrySet()
                .stream()
                .map(e -> e.getValue().stream()
                        .sorted((a, b) -> b.getCreated().compareTo(a.getCreated()))
                        .findFirst())
                .map(Optional::get)
                .collect(Collectors.toList());
        if (m.size() > 0) { // No point sending empty messages
            transport.sendMessage(TopicFactory.monitoring(), DisplayDirectMessageFactory.toMonitoringMetrics(m), QoS.AT_LEAST_ONCE);
        }
    }

    public static void stop() {
        schedule.shutdown();
    }
}
