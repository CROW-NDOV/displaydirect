package nl.crowndov.displaydirect.commonclient.domain;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.configuration.DisplayParameters;
import nl.crowndov.displaydirect.commonclient.configuration.SystemParameters;

/**
 * Copyright 2017 CROW-NDOV
 * <p>
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class SubscriptionBuilder {

    public static DisplayDirectMessage.Subscribe subscribe(DisplayParameters display, SystemParameters system) {
        DisplayDirectMessage.Subscribe.Builder build = DisplayDirectMessage.Subscribe.newBuilder()
                .addAllStopCode(system.getStopCodes())
                .setDisplayProperties(DisplayDirectMessage.Subscribe.DisplayProperties.newBuilder().setTextCharacters(0))
                .setFieldFilter(DisplayDirectMessage.Subscribe.FieldFilter.newBuilder()
                        .setTripStopStatus(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setTargetDepartureTime(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setDestination(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setLinePublicNumber(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setTransportType(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS))
                .setEmail(system.getEmail())
                .setDescription(system.getDescription());

        return build.build();
    }

    public static DisplayDirectMessage.Unsubscribe unsubscribe() {
        return DisplayDirectMessage.Unsubscribe.newBuilder().setIsPermanent(false).build();
    }
}
