package nl.crowndov.displaydirect.common.transport.mqtt;

import java.util.Optional;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TopicParser {

    private static final String UNSUBSCRIBE = "unsubscribe";
    private static final String SUBSCRIBE = "subscribe";

    public static Optional<String> getId(String topic) {
        if (topic.startsWith(SUBSCRIBE) || topic.startsWith(UNSUBSCRIBE)) {
            return Optional.of(topic.split("\\/")[2]);
        }
        return Optional.empty();
    }

    public static Optional<String> getCommand(String topic) {
        if (topic.startsWith(SUBSCRIBE)) {
            return Optional.of(SUBSCRIBE);
        } else if (topic.startsWith(UNSUBSCRIBE)) {
            return Optional.of(UNSUBSCRIBE);
        }
        return Optional.empty();
    }
}
