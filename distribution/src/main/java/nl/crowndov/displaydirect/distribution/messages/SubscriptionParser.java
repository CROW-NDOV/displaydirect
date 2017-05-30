package nl.crowndov.displaydirect.distribution.messages;

import nl.crowndov.displaydirect.distribution.domain.client.Subscription;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class SubscriptionParser {

    public static Subscription toSubscription(String id, DisplayDirectMessage.Subscribe input) {
        Subscription out = new Subscription(id);
        if (input.getDisplayProperties().getTextCharacters() != 0) {
            out.setTextCharacters(input.getDisplayProperties().getTextCharacters());
        }
        if (input.getDisplayProperties().getLines() != 0) {
            out.setLines(input.getDisplayProperties().getLines());
        }

        DisplayDirectMessage.Subscribe.FieldFilter f = input.getFieldFilter();
        out.setTargetArrivalTime(translate(f.getTargetArrivalTime()));
        out.setTargetDepartureTime(translate(f.getTargetDepartureTime()));
        out.setExpectedArrivalTime(translate(f.getExpectedArrivalTime()));
        out.setNumberOfCoaches(translate(f.getNumberOfCoaches()));
        out.setTripStopStatus(translate(f.getTripStopStatus()));
        out.setTransportType(translate(f.getTransportType()));
        out.setWheelchairAccessible(translate(f.getWheelchairAccessible()));
        out.setIsTimingStop(translate(f.getIsTimingStop()));
        out.setStopCode(translate(f.getStopCode()));
        out.setDestination(translate(f.getDestination()));
        out.setLinePublicNumber(translate(f.getLinePublicNumber()));
        out.setSideCode(translate(f.getSideCode()));
        out.setJourneyMessageContent(translate(f.getJourneyMessageContent()));
        out.setLineDirection(translate(f.getLineDirection()));
        out.setLineColor(translate(f.getLineColor()));
        out.setLineTextColor(translate(f.getLineTextColor()));
        out.setLineIcon(translate(f.getLineIcon()));
        out.setDestinationColor(translate(f.getDestinationColor()));
        out.setDestinationTextColor(translate(f.getDestinationTextColor()));
        out.setDestinationIcon(translate(f.getDestinationIcon()));
        out.setGeneratedTimestamp(translate(f.getGeneratedTimestamp()));

        out.setSubscribedQuayCodes(input.getStopCodeList());
        out.setEmail(checkEmpty(input.getEmail()));
        out.setDescription(checkEmpty(input.getDescription()));

        return out;
    }

    private static String checkEmpty(String input) {
        return (input == null || input.isEmpty()) ? null : input;
    }

    private static Subscription.FieldDelivery translate(DisplayDirectMessage.Subscribe.FieldFilter.Delivery destination) {
        switch (destination) {
            case DELTA:
                return Subscription.FieldDelivery.DELTA;
            case ALWAYS:
                return Subscription.FieldDelivery.ALWAYS;
            case NEVER:
                return Subscription.FieldDelivery.NEVER;
            default:
                return Subscription.FieldDelivery.ALWAYS;
        }
    }
}
