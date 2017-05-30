package nl.crowndov.displaydirect.distribution.chb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class StopStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopStore.class);

    private static Map<String, String> store = new HashMap<>();
    private static Set<String> missing = new HashSet<>();

    public static Optional<String> getQuayFromCode(String dataOwner, String stopCode) {
        String key = dataOwner+"|"+stopCode;
        if (store.containsKey(key)) {
            return Optional.of(store.get(key));
        }
        missing.add(key);
        return Optional.empty();
    }

    public static boolean stopExists(String quayOrStopCode) {
        return store.values().contains(quayOrStopCode);
    }

    public static Map<String, String> getStore() {
        return store;
    }

    public static Set<String> getMissing() {
        return missing;
    }

    public static void setStore(Map<String, String> storeData) {
        store = storeData;
    }
}
