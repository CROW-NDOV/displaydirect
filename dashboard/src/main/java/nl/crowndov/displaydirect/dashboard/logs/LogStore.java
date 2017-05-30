package nl.crowndov.displaydirect.dashboard.logs;

import nl.crowndov.displaydirect.common.stats.domain.LogMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class LogStore {

    private static List<LogMessage> logs = new ArrayList<>();
    private static Map<String, List<LogMessage>> stopLogs = new HashMap<>();

    public static List<LogMessage> get() {
        return logs;
    }

    public static void add(LogMessage in) {
        // HashTable?
        if (in.getStopSystemId() != null && !in.getStopSystemId().isEmpty()) {
            if (!stopLogs.containsKey(in.getStopSystemId())) {
                stopLogs.put(in.getStopSystemId(), new ArrayList<>());
            }
            stopLogs.get(in.getStopSystemId()).add(in);
        }
        logs.add(in);
    }


    public static List<LogMessage> get(String stopSystemId) {
        return stopLogs.getOrDefault(stopSystemId, new ArrayList<>());
    }
}
