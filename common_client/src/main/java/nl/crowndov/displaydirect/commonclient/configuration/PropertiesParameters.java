package nl.crowndov.displaydirect.commonclient.configuration;

/**
 * Copyright 2017 CROW-NDOV
 * <p>
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class PropertiesParameters implements DisplayParameters {
    @Override
    public long getDestinationAlternatingSeconds() {
        return 0;
    }

    @Override
    public long getJourneyMinimumDepartureMinutes() {
        return 0;
    }

    @Override
    public long getMaxCombinedDirections() {
        return 0;
    }

    @Override
    public boolean hideJourneyOnArrival() {
        return false;
    }

    @Override
    public long getDepartureTimeoutSeconds() {
        return 0;
    }

    @Override
    public long getJourneyTimeoutSeconds() {
        return 0;
    }

    @Override
    public long getUnplannedJourneyTimeoutSeconds() {
        return 0;
    }

    @Override
    public boolean showCancelledTrip() {
        return false;
    }

    @Override
    public boolean showBlankLine() {
        return false;
    }

    @Override
    public int getGeneralMessageTimeoutMinutes() {
        return 0;
    }

    @Override
    public int getMqttKeepaliveTime() {
        return 0;
    }
}
