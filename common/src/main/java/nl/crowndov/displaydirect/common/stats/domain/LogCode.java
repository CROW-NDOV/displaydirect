package nl.crowndov.displaydirect.common.stats.domain;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public enum LogCode {

    SYSTEM_STARTED(100),
    SYSTEM_STOPPED(101),

    PLANNING_LOADED(500),
    PLANNING_STARTED_LOADING(501),
    PLANING_LOADING_ERROR(502),

    SUBSCRIPTION_ADDED(1000),
    SUBSCRIPTION_REMOVED(1001),
    SUBSCRIPTION_INVALID(1010),
    SUBSCRIPTION_INVALID_UNSUBSCRIBE(1020),

    AUTHORISATION_SENT(1100),
    AUTHORISATION_VALIDATED(1101);

    private final int code;

    LogCode(int code) {
        this.code = code;
    }

    public int getValue() {
        return code;
    }

    public static LogCode fromCode(int code) {
        for (LogCode lc : values()) {
            if (lc.getValue() == code) {
                return lc;
            }
        }
        return null;
    }
}
