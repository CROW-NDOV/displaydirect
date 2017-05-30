package nl.crowndov.displaydirect.common.transport.mqtt;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class TopicFactory {

    /**
     * Generate a topic for subscribe messages
     * @param stopSytemId An id for the originating stopsystem. If null, will be filled with a wildcard for that level
     * @return A valid topic name for subscribe messages
     */
    public static String subscribe(String stopSytemId) {
        return stopSystemOrigin(stopSytemId, "subscribe");
    }

    /**
     * Generate a topic for unsubscribe messages
     * @param stopSytemId An id for the originating stopsystem. If null, will be filled with a wildcard for that level
     * @return A valid topic name for unsubscribe messages
     */
    public static String unsubscribe(String stopSytemId) {
        return stopSystemOrigin(stopSytemId, "unsubscribe");
    }

    /**
     * Generate a topic for travel information messages
     * @param stopSytemId An id for the destination stopsystem. If null, will be filled with a wildcard for that level
     * @return A valid topic name for travel information messages
     */
    public static String travelInformation(String stopSytemId) {
        return stopSystemDestination(stopSytemId, "travel_information");
    }

    /**
     * Generate a topic for subscription response messages
     * @param stopSytemId An id for the destination stopsystem. If null, will be filled with a wildcard for that level
     * @return A valid topic name for subscription response messages
     */
    public static String subscriptionResponse(String stopSytemId) {
        return stopSystemDestination(stopSytemId, "subscription_response");
    }

    /**
     * Generate a topic for monitoring messages about stops
     * @param stopSytemId An id for the object stopsystem. If null, will be filled with a wildcard for that level
     * @return A valid topic name for monitoring messages
     */
    public static String monitoring(String stopSytemId) {
        return stopSystemDestination(stopSytemId, "monitoring");
    }

    /**
     * Generate a topic for monitoring messages about the distribution function
     * @return A valid topic name for monitoring messages
     */
    public static String monitoring() {
        return "distribution/monitoring";
    }

    private static String stopSystemDestination(String stopSytemId, String category) {
        return "stopsystem/"+ idOrWildcard(stopSytemId) + "/" + category;
    }

    private static String stopSystemOrigin(String stopSytemId, String command) {
        return command + "/stopsystem/"+ idOrWildcard(stopSytemId);
    }

    private static String idOrWildcard(String id) {
        return id != null ? id : "+";
    }

}
