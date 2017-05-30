package nl.crowndov.displaydirect.commonclient.client;

import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DisplayConfiguration {

    public static long getMaxLines() {
        return 11;
    }

    /* ALTERBEST */
    public static long getDestinationAlternatingSeconds() { return 5; }

    /* TOONTIJD */
    public static long getJourneyMinimumDepartureMinutes() { return 59; }

    /* TOONMAX */
    public static long getMaxCombinedDirections() { return 2; }

    /* SNELWIS */
    public static boolean hideJourneyOnArrival() { return true; }

    /* VERTREKTIMEOUT */
    public static long getDepartureTimeoutSeconds() { return 120; }

    /* RITRESET */
    public static long getJourneyTimeoutSeconds() { return 30; }

    /* INDICATORVANAF */
    public static long getUnplannedJourneyTimeoutSeconds() { return 0; }

    /* VERVALTWEERGAVE */
    public static boolean showCancelledTrip() { return false; }

    /* WITREGEL  */
    public static boolean showBlankLine() { return true; }

    /* VRIJETEKSTTIMEOUT */
    public static int getGeneralMessageTimeoutMinutes() { return 722; }

    /* KEEPALIVETIME */
    public static int getMqttTimeout() { return 90; }

    public static List<String> getStopCodes() {
        return Collections.singletonList("NL:Q:50000360");
    }

    public static ZoneId getTimezone() {
        return ZoneId.of("UTC");
    }
}
