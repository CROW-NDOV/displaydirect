package nl.crowndov.displaydirect.distribution.stats.logging;

import nl.crowndov.displaydirect.common.stats.domain.LogCode;
import nl.crowndov.displaydirect.common.stats.domain.LogMessage;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.distribution.messages.DisplayDirectMessageFactory;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Log {

    private static Transport transport = TransportFactory.get();
    private static String topic = TopicFactory.monitoring();

    public static void send(LogCode code, String message) {
        send(new LogMessage(code, message));
    }

    public static void send(LogCode code, String stopSystemId, String message) {
        send(new LogMessage(code, stopSystemId, message));
    }

    public static void send(LogMessage lm) {
        if (lm.getStopSystemId() == null) {
            transport.sendMessage(topic, DisplayDirectMessageFactory.toMonitoringLog(lm));
        } else {
            transport.sendMessage(TopicFactory.monitoring(lm.getStopSystemId()), DisplayDirectMessageFactory.toMonitoringLog(lm));
        }
    }
}
