package nl.crowndov.displaydirect.distribution.messages;

import nl.crowndov.displaydirect.distribution.domain.client.Subscription;
import nl.crowndov.displaydirect.distribution.stats.MetricStore;
import nl.crowndov.displaydirect.distribution.util.AbstractService;
import nl.crowndov.displaydirect.distribution.util.Store;

import java.util.*;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class SubscriptionStore extends AbstractService {

    private static final MetricStore metrics = MetricStore.getInstance();

    // TODO: Guava table thing
    private static Store.SubscriptionQuayStore quayIndex = new Store.SubscriptionQuayStore();
    private static Store.SubscriptionStore systemIndex = new Store.SubscriptionStore();

    public static void start() {
        readFile("subscriptions", Store.SubscriptionStore.class).ifPresent(subs -> {
            systemIndex = subs;
            subs.values().forEach(SubscriptionStore::populateQuayIndexFromSub); // Rebuild the list of quays
            // TODO: Do something with planning here to ensure the stopsystems have latest & greatest?
        });
    }

    public static void stop() {
        writeFile("subscriptions", systemIndex);
    }

    public static boolean hasSubscription(String quay) {
        return quayIndex.containsKey(quay);
    }

    public static boolean isSystemSubscribed(String stopSystemId) {
        // TODO: Also check authorization system for waiting subscriptions
        return systemIndex.containsKey(stopSystemId);
    }

    public static int getSubscribedStopCount() {
        return quayIndex.keySet().size();
    }

    public static List<Subscription> getForQuay(String quay) {
        if (quayIndex.containsKey(quay)) {
            return quayIndex.get(quay);
        }
        return new ArrayList<>();
    }

    public static Optional<Subscription> getForSystem(String id) {
        if (systemIndex.containsKey(id)) {
            return Optional.of(systemIndex.get(id));
        }
        return Optional.empty();
    }

    // TODO: Think about multithreading
    public static synchronized void add(Subscription sub) {
        populateQuayIndexFromSub(sub);
        systemIndex.put(sub.getId(), sub); // This overwrites existing entries
        updateStats();
    }

    private static void populateQuayIndexFromSub(Subscription sub) {
        sub.getSubscribedQuayCodes().forEach(quay -> {
            if (!quayIndex.containsKey(quay)) {
                quayIndex.put(quay, new ArrayList<>());
            }
            quayIndex.get(quay).add(sub);
        });
    }

    public static synchronized void remove(String id) {
        if (systemIndex.containsKey(id)) {
            Subscription s = systemIndex.get(id);
            s.getSubscribedQuayCodes().forEach(quay -> {
                List<Subscription> quays = quayIndex.get(quay);
                if (quays != null) {
                    quays.remove(s);
                    // Cleanup
                    if (quays.size() == 0) {
                        quayIndex.remove(quay);
                    }
                }
            });
            systemIndex.remove(id);
            updateStats();
        }
    }


    private static void updateStats() {
        metrics.storeMetric("subscriptions.active", systemIndex.size());
        metrics.storeMetric("subscriptions.stops", quayIndex.size());
    }

    public static  Map<String, Subscription> getAllSystems() {
        return systemIndex;
    }

    public static  Map<String, List<Subscription>> getAllQuays() {
        return quayIndex;
    }
}
