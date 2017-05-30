package nl.crowndov.displaydirect.common.stats.domain;

import java.time.LocalDateTime;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class LogMessage {

    private final LogCode code;
    private final String message;
    private final String stopSystemId;
    private final LocalDateTime timestamp;

    public LogMessage(LogCode code, String message) {
        this.code = code;
        this.message = message;
        this.stopSystemId = null;
        this.timestamp = LocalDateTime.now();
    }

    public LogMessage(LogCode code, String stopSystemId, String message) {
        this.code = code;
        this.message = message;
        this.stopSystemId = stopSystemId;
        this.timestamp = LocalDateTime.now();
    }

    public LogMessage(LogCode code, String stopSystemId, String message, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.stopSystemId = stopSystemId;
        this.timestamp = timestamp;
    }

    public LogMessage(LogCode code, String message, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
        this.stopSystemId = null;
    }

    public LogCode getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getStopSystemId() {
        return stopSystemId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
