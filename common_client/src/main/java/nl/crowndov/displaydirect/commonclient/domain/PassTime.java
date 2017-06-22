package nl.crowndov.displaydirect.commonclient.domain;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.client.DisplayConfiguration;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class PassTime {

    private int passTimeHash;

    private int targetArrivalTime;
    private int targetDepartureTime;

    private int expectedArrivalTime;
    private int expectedDepartureTime;

    private DisplayDirectMessage.PassingTimes.TripStopStatus status;

    private List<String> destination;
    private String transportType;

    private String lineNumber;

    public int getPassTimeHash() {
        return passTimeHash;
    }

    public int getTargetArrivalTime() {
        return targetArrivalTime;
    }

    public int getTargetDepartureTime() {
        return targetDepartureTime;
    }

    public int getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public int getExpectedDepartureTime() {
        return expectedDepartureTime;
    }

    public List<String> getDestination() {
        return destination;
    }

    public String getTransportType() {
        return transportType;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public DisplayDirectMessage.PassingTimes.TripStopStatus getStatus() {
        return status;
    }

    private PassTime(Builder builder) {
        passTimeHash = builder.passTimeHash;
        targetArrivalTime = builder.targetArrivalTime;
        targetDepartureTime = builder.targetDepartureTime;
        expectedArrivalTime = builder.expectedArrivalTime;
        expectedDepartureTime = builder.expectedDepartureTime;
        status = builder.status;
        destination = builder.destination;
        transportType = builder.transportType;
        lineNumber = builder.lineNumber;
    }

    public boolean isCurrent() {
        return status != DisplayDirectMessage.PassingTimes.TripStopStatus.PASSED && status != DisplayDirectMessage.PassingTimes.TripStopStatus.DELETED
                && status != DisplayDirectMessage.PassingTimes.TripStopStatus.CANCELLED  && status != DisplayDirectMessage.PassingTimes.TripStopStatus.SKIPPED &&
                isWithinDisplayPeriod() && !isDisplayedTooLongActual() && !isDisplayedTooLongPlanned();
    }

    private boolean isWithinDisplayPeriod() {
        return Math.abs(secondsToExpectedDeparture()/60) < DisplayConfiguration.getDisplayParameters().getJourneyMinimumDepartureMinutes();
    }

    private boolean isDisplayedTooLongPlanned() {
        return (status == DisplayDirectMessage.PassingTimes.TripStopStatus.PLANNED || status == DisplayDirectMessage.PassingTimes.TripStopStatus.UNKNOWN)
                && secondsToExpectedDeparture() > DisplayConfiguration.getDisplayParameters().getUnplannedJourneyTimeoutSeconds();
    }

    private boolean isDisplayedTooLongActual() {
        return ((status == DisplayDirectMessage.PassingTimes.TripStopStatus.DRIVING || status == DisplayDirectMessage.PassingTimes.TripStopStatus.ARRIVED) &&
                secondsToExpectedDeparture() > DisplayConfiguration.getDisplayParameters().getJourneyTimeoutSeconds());
    }

    private long secondsToExpectedDeparture() {
        return ZonedDateTime.now(DisplayConfiguration.getTimezone()).toEpochSecond() - expectedDepartureTime;
    }

    @Override
    public String toString() {
        return lineNumber+" "+destination+"  "+Math.round((ZonedDateTime.now(DisplayConfiguration.getTimezone()).toEpochSecond() - expectedDepartureTime)/60);
    }

    public static final class Builder {
        private int passTimeHash;
        private int targetArrivalTime;
        private int targetDepartureTime;
        private int expectedArrivalTime;
        private int expectedDepartureTime;
        private DisplayDirectMessage.PassingTimes.TripStopStatus status;
        private List<String> destination;
        private String transportType;
        private String lineNumber;

        public Builder() {
        }

        public Builder passTimeHash(int val) {
            passTimeHash = val;
            return this;
        }

        public Builder targetArrivalTime(int val) {
            targetArrivalTime = val;
            return this;
        }

        public Builder targetDepartureTime(int val) {
            targetDepartureTime = val;
            return this;
        }

        public Builder expectedArrivalTime(int val) {
            expectedArrivalTime = val;
            return this;
        }

        public Builder expectedDepartureTime(int val) {
            expectedDepartureTime = val;
            return this;
        }

        public Builder status(DisplayDirectMessage.PassingTimes.TripStopStatus val) {
            status = val;
            return this;
        }

        public Builder destination(List<String> val) {
            destination = val;
            return this;
        }

        public Builder transportType(String val) {
            transportType = val;
            return this;
        }

        public Builder lineNumber(String val) {
            lineNumber = val;
            return this;
        }

        public PassTime build() {
            return new PassTime(this);
        }
    }
}
