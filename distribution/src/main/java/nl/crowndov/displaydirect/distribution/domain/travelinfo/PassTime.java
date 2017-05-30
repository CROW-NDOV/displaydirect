package nl.crowndov.displaydirect.distribution.domain.travelinfo;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class PassTime extends RealtimeMessage implements Serializable {

    private static final long serialVersionUID = 8458255587785977161L;

    private String linePlanningNumber;
    private Line line;
    private int journeyNumber;

    private int targetArrivalTime;
    private int targetDepartureTime;
    private int expectedArrivalTime;
    private int expectedDepartureTime;

    private int numberOfCoaches;
    private Status tripStopStatus = Status.PLANNED;
    private Boolean wheelchairAccessible;

    private String destinationCode;
    private Destination destination;
    private String sideCode;

    private int generatedTimestamp;
    private Boolean isTimingStop;
    private int lineDirection;

    public PassTime(String dataOwnerCode, LocalDate date, String linePlanningNumber, int journeyNumber, String stopCode) {
        super(Type.PASSTIME, date, dataOwnerCode, stopCode);
        this.linePlanningNumber = linePlanningNumber;
        this.journeyNumber = journeyNumber;
    }

    public String getLinePlanningNumber() {
        return linePlanningNumber;
    }

    public int getJourneyNumber() {
        return journeyNumber;
    }

    public void setTargetArrivalTime(int targetArrivalTime) {
        this.targetArrivalTime = targetArrivalTime;
    }

    public void setTargetDepartureTime(int targetDepartureTime) {
        this.targetDepartureTime = targetDepartureTime;
    }

    public void setExpectedArrivalTime(int expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    public void setExpectedDepartureTime(int expectedDepartureTime) {
        this.expectedDepartureTime = expectedDepartureTime;
    }

    public void setNumberOfCoaches(int numberOfCoaches) {
        this.numberOfCoaches = numberOfCoaches;
    }

    public void setTripStopStatus(Status tripStopStatus) {
        this.tripStopStatus = tripStopStatus;
    }

    public void setWheelchairAccessible(Boolean wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public void setDestinationCode(String destinationCode) {
        this.destinationCode = destinationCode;
    }

    public void setSideCode(String sideCode) {
        this.sideCode = sideCode;
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

    public int getNumberOfCoaches() {
        return numberOfCoaches;
    }

    public Status getTripStopStatus() {
        return tripStopStatus;
    }

    public Boolean getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public String getDestinationCode() {
        return destinationCode;
    }

    public String getSideCode() {
        return sideCode;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getPassTimeKey() {
        return String.format("%s|%s|%s|%s|%s", getDataOwnerCode(), getDate(), linePlanningNumber, journeyNumber, getStopCode());
    }

    public int getPassTimeHash() {
        return getPassTimeKey().hashCode();
    }

    public void setGeneratedTimestamp(int generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }

    public int getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public Boolean getIsTimingStop() {
        return isTimingStop;
    }

    public int getLineDirection() {
        return lineDirection;
    }

    public void setTimingStop(Boolean timingStop) {
        isTimingStop = timingStop;
    }

    public void setLineDirection(int lineDirection) {
        this.lineDirection = lineDirection;
    }

    public enum Status {
        PLANNED,
        UNKNOWN,
        DRIVING,
        ARRIVED,
        PASSED,
        CANCEL
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PassTime passTime = (PassTime) o;

        if (journeyNumber != passTime.journeyNumber) return false;
        if (targetArrivalTime != passTime.targetArrivalTime) return false;
        if (targetDepartureTime != passTime.targetDepartureTime) return false;
        if (expectedArrivalTime != passTime.expectedArrivalTime) return false;
        if (expectedDepartureTime != passTime.expectedDepartureTime) return false;
        if (numberOfCoaches != passTime.numberOfCoaches) return false;
        if (linePlanningNumber != null ? !linePlanningNumber.equals(passTime.linePlanningNumber) : passTime.linePlanningNumber != null)
            return false;
        if (tripStopStatus != passTime.tripStopStatus) return false;
        if (wheelchairAccessible != null ? !wheelchairAccessible.equals(passTime.wheelchairAccessible) : passTime.wheelchairAccessible != null)
            return false;
        if (destinationCode != null ? !destinationCode.equals(passTime.destinationCode) : passTime.destinationCode != null)
            return false;
        return sideCode != null ? sideCode.equals(passTime.sideCode) : passTime.sideCode == null;
    }

    @Override
    public int hashCode() {
        int result = linePlanningNumber != null ? linePlanningNumber.hashCode() : 0;
        result = 31 * result + journeyNumber;
        result = 31 * result + targetArrivalTime;
        result = 31 * result + targetDepartureTime;
        result = 31 * result + expectedArrivalTime;
        result = 31 * result + expectedDepartureTime;
        result = 31 * result + numberOfCoaches;
        result = 31 * result + (tripStopStatus != null ? tripStopStatus.hashCode() : 0);
        result = 31 * result + (wheelchairAccessible != null ? wheelchairAccessible.hashCode() : 0);
        result = 31 * result + (destinationCode != null ? destinationCode.hashCode() : 0);
        result = 31 * result + (sideCode != null ? sideCode.hashCode() : 0);
        return result;
    }
}
