package nl.crowndov.displaydirect.distribution.input;

import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.kv78.Kv78Parser;
import nl.crowndov.displaydirect.distribution.kv78.RealtimeMessageMapper;
import nl.crowndov.displaydirect.distribution.messages.DisplayDirectMessageFactory;
import nl.crowndov.displaydirect.distribution.messages.SubscriptionStore;
import nl.crowndov.displaydirect.distribution.messages.processing.ProcessFactory;
import nl.crowndov.displaydirect.distribution.stats.MetricStore;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 * <p>
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class InputHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputHandler.class);

    private static ProcessFactory factory = new ProcessFactory();
    private static MetricStore metrics = MetricStore.getInstance();
    private static Transport transport = TransportFactory.get();

    public static void handleMessage(String m) {
        Map<String, List<RealtimeMessage>> realtime = RealtimeMessageMapper.toRealtimeMessage(Kv78Parser.parseMessage(m))
                .stream()
                .map(r -> factory.process(r))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(RealtimeMessage::getQuayCode));
        realtime.forEach((key, value) -> {
            SubscriptionStore.getForQuay(key).forEach(sub -> {
                if (value.size() > 0) {
                    LOGGER.trace("Publishing on topic '/travel_information' to '{}'", sub.getId());
                    metrics.increaseBucketValue("kv78turbo.messages.sent", ChronoUnit.HOURS);
                    byte[] msg = DisplayDirectMessageFactory.fromRealTime(value, sub);
                    if (msg != null) {
                        transport.sendMessage(TopicFactory.travelInformation(sub.getId()), msg, QoS.AT_LEAST_ONCE);
                    } else {
                        LOGGER.debug("Got zero result for msg {}, translated to {} and sub {}", m, value, sub.getId());
                    }
                }
            });
        });
    }
}
