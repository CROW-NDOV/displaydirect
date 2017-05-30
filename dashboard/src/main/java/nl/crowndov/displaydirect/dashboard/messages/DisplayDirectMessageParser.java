package nl.crowndov.displaydirect.dashboard.messages;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.stats.domain.LogCode;
import nl.crowndov.displaydirect.common.stats.domain.LogMessage;
import nl.crowndov.displaydirect.common.stats.domain.Metric;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DisplayDirectMessageParser {

    public static LogMessage toLogMessage(DisplayDirectMessage.Monitoring.LogMessage m) {
        return new LogMessage(LogCode.fromCode(m.getCode()), m.getStopsystemId(), m.getMessage(), timestampToDateTime(m.getTimestamp()));
    }

    public static Metric toMetric(DisplayDirectMessage.Monitoring.Metric m) {
        return new Metric(m.getName(), m.getValue(), timestampToDateTime(m.getTimestamp()));
    }

    private static LocalDateTime timestampToDateTime(int timestamp) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC);
    }
}
