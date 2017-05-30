package nl.crowndov.displaydirect.distribution.util;

import nl.crowndov.displaydirect.distribution.authorization.domain.ValidationToken;
import nl.crowndov.displaydirect.distribution.domain.QuayStore;
import nl.crowndov.displaydirect.distribution.domain.client.Subscription;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.Destination;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Store {

    public static class StringList extends ArrayList<String> {}

    public static class ValidationTokenStore extends ConcurrentHashMap<String, ValidationToken> {}

    public static class PlanningStore extends ConcurrentHashMap<String, QuayStore> {}

    public static class LineStore extends ConcurrentHashMap<String, Line> {}

    public static class DestinationStore extends ConcurrentHashMap<String, Destination> {}

    public static class SubscriptionQuayStore extends ConcurrentHashMap<String, List<Subscription>> {}

    public static class SubscriptionStore extends ConcurrentHashMap<String, Subscription> {}

    public static class TimingPointStore extends ConcurrentHashMap<String, String> {}
}
