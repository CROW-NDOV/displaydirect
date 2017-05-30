package nl.crowndov.displaydirect.commonclient.client;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DisplayDirectParser {

    public static List<PassTime> fromContainer(DisplayDirectMessage.Container c) {
        List<PassTime> list = new ArrayList<>();
        for (DisplayDirectMessage.PassingTimes pt : c.getPassingTimesList()) {
            for (int i = 0; i < pt.getPassTimeHashCount(); i += 1) {
                list.add(new PassTime.Builder()
                        .passTimeHash(pt.getPassTimeHash(i))
                        .expectedDepartureTime(pt.getExpectedDepartureTime(i))
                        .targetArrivalTime(pt.getExpectedDepartureTime(i))
                        .targetDepartureTime(pt.getTargetDepartureTime(i))
                        .transportType(pt.getTransportType(i).name())
                        .lineNumber(pt.getLinePublicNumber(i))
                        .status(pt.getTripStopStatus(i))
                        .destination(pt.getDestination(i).getValueList())
                        .build());
            }
        }
        return list;
    }
}
