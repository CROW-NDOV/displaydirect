package nl.crowndov.displaydirect.commonclient.configuration;

/**
 * An abstract configuration contains the defaults that are valid for every display system and can be overriden.
 * A configuration consists of two sets of parameters: those for the system in general and those for the display
 * of travel information on a screen.
 *
 * Copyright 2017 CROW-NDOV
 * <p>
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public abstract class AbstractConfiguration implements DisplayParameters, SystemParameters {
    private String clientId;

    @Override
    public long getDestinationAlternatingSeconds() {
        return 5;
    }

    @Override
    public long getJourneyMinimumDepartureMinutes() {
        return 59;
    }

    @Override
    public long getMaxCombinedDirections() {
        return 2;
    }

    @Override
    public boolean hideJourneyOnArrival() {
        return true;
    }

    @Override
    public long getDepartureTimeoutSeconds() {
        return 120;
    }

    @Override
    public long getJourneyTimeoutSeconds() {
        return 30;
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
        return true;
    }

    @Override
    public int getGeneralMessageTimeoutMinutes() {
        return 722;
    }

    @Override
    public int getMqttKeepaliveTime() {
        return 90;
    }

    public String getSessionId() {
        return getClientGroup() + "_" + getClientId();
    }

}
