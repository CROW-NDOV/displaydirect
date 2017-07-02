package nl.crowndov.displaydirect.distribution.domain.client;

import nl.crowndov.displaydirect.distribution.chb.StopStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Subscription {

    private String id;

    private int lines = 0;
    private int textCharacters = 0;

    private List<String> subscribedQuayCodes = new ArrayList<>();

    private FieldDelivery targetArrivalTime;
    private FieldDelivery targetDepartureTime;
    private FieldDelivery expectedArrivalTime;
    private FieldDelivery numberOfCoaches;
    private FieldDelivery tripStopStatus;
    private FieldDelivery transportType;
    private FieldDelivery wheelchairAccessible;
    private FieldDelivery isTimingStop;
    private FieldDelivery stopCode;
    private FieldDelivery destination;
    private FieldDelivery linePublicNumber;
    private FieldDelivery sideCode;
    private FieldDelivery lineDirection;
    private FieldDelivery journeyMessageContent;
    private FieldDelivery lineColor;
    private FieldDelivery lineTextColor;
    private FieldDelivery lineIcon;
    private FieldDelivery destinationColor;
    private FieldDelivery destinationTextColor;
    private FieldDelivery destinationIcon;
    private FieldDelivery generatedTimestamp;

    private String email;
    private String description;

    public Subscription(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getTextCharacters() {
        return textCharacters;
    }

    public void setTextCharacters(int textCharacters) {
        this.textCharacters = textCharacters;
    }

    public List<String> getSubscribedQuayCodes() {
        return subscribedQuayCodes;
    }

    public void setSubscribedQuayCodes(List<String> subscribedQuayCodes) {
        this.subscribedQuayCodes = subscribedQuayCodes;
    }

    public FieldDelivery getTargetArrivalTime() {
        return targetArrivalTime;
    }

    public void setTargetArrivalTime(FieldDelivery targetArrivalTime) {
        this.targetArrivalTime = targetArrivalTime;
    }

    public FieldDelivery getTargetDepartureTime() {
        return targetDepartureTime;
    }

    public void setTargetDepartureTime(FieldDelivery targetDepartureTime) {
        this.targetDepartureTime = targetDepartureTime;
    }

    public FieldDelivery getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public void setExpectedArrivalTime(FieldDelivery expectedArrivalTime) {
        this.expectedArrivalTime = expectedArrivalTime;
    }

    public FieldDelivery getNumberOfCoaches() {
        return numberOfCoaches;
    }

    public void setNumberOfCoaches(FieldDelivery numberOfCoaches) {
        this.numberOfCoaches = numberOfCoaches;
    }

    public FieldDelivery getTripStopStatus() {
        return tripStopStatus;
    }

    public void setTripStopStatus(FieldDelivery tripStopStatus) {
        this.tripStopStatus = tripStopStatus;
    }

    public FieldDelivery getTransportType() {
        return transportType;
    }

    public void setTransportType(FieldDelivery transportType) {
        this.transportType = transportType;
    }

    public FieldDelivery getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(FieldDelivery wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public FieldDelivery getIsTimingStop() {
        return isTimingStop;
    }

    public void setIsTimingStop(FieldDelivery isTimingStop) {
        this.isTimingStop = isTimingStop;
    }

    public FieldDelivery getStopCode() {
        return stopCode;
    }

    public void setStopCode(FieldDelivery stopCode) {
        this.stopCode = stopCode;
    }

    public FieldDelivery getDestination() {
        return destination;
    }

    public void setDestination(FieldDelivery destination) {
        this.destination = destination;
    }

    public FieldDelivery getLinePublicNumber() {
        return linePublicNumber;
    }

    public void setLinePublicNumber(FieldDelivery linePublicNumber) {
        this.linePublicNumber = linePublicNumber;
    }

    public FieldDelivery getSideCode() {
        return sideCode;
    }

    public void setSideCode(FieldDelivery sideCode) {
        this.sideCode = sideCode;
    }

    public FieldDelivery getLineDirection() {
        return lineDirection;
    }

    public void setLineDirection(FieldDelivery lineDirection) {
        this.lineDirection = lineDirection;
    }

    public FieldDelivery getJourneyMessageContent() {
        return journeyMessageContent;
    }

    public void setJourneyMessageContent(FieldDelivery journeyMessageContent) {
        this.journeyMessageContent = journeyMessageContent;
    }

    public FieldDelivery getLineColor() {
        return lineColor;
    }

    public void setLineColor(FieldDelivery lineColor) {
        this.lineColor = lineColor;
    }

    public FieldDelivery getLineTextColor() {
        return lineTextColor;
    }

    public void setLineTextColor(FieldDelivery lineTextColor) {
        this.lineTextColor = lineTextColor;
    }

    public FieldDelivery getLineIcon() {
        return lineIcon;
    }

    public void setLineIcon(FieldDelivery lineIcon) {
        this.lineIcon = lineIcon;
    }

    public FieldDelivery getDestinationColor() {
        return destinationColor;
    }

    public void setDestinationColor(FieldDelivery destinationColor) {
        this.destinationColor = destinationColor;
    }

    public FieldDelivery getDestinationTextColor() {
        return destinationTextColor;
    }

    public void setDestinationTextColor(FieldDelivery destinationTextColor) {
        this.destinationTextColor = destinationTextColor;
    }

    public FieldDelivery getDestinationIcon() {
        return destinationIcon;
    }

    public void setDestinationIcon(FieldDelivery destinationIcon) {
        this.destinationIcon = destinationIcon;
    }

    public FieldDelivery getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public void setGeneratedTimestamp(FieldDelivery generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return email != null && subscribedQuayCodes != null && subscribedQuayCodes.size() >= 1;
    }

    public boolean hasValidStop() {
        return subscribedQuayCodes.stream().allMatch(StopStore::stopExists);
    }

    public String getPrefix() {
        return id.split("_", 2)[0];
    }

    public enum FieldDelivery {
        NEVER,
        DELTA,
        ALWAYS
    }
}
