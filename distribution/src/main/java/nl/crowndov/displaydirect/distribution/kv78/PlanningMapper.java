package nl.crowndov.displaydirect.distribution.kv78;

import nl.crowndov.displaydirect.distribution.chb.StopStore;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.*;
import nl.crowndov.displaydirect.distribution.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class PlanningMapper {

    public static PassTime passTime(LocalDate date, Map<String, String> r, ZonedDateTime generated) {
        PassTime pt = new PassTime(r.get("DataOwnerCode"), date,
                r.get("LinePlanningNumber"), Integer.valueOf(r.get("JourneyNumber")), r.get("UserStopCode"));
        pt.setTargetArrivalTime((int) DateTimeUtil.getTime(date, r.get("TargetArrivalTime")).toEpochSecond());
        pt.setTargetDepartureTime((int) DateTimeUtil.getTime(date, r.get("TargetDepartureTime")).toEpochSecond());

        if ("ACCESSIBLE".contentEquals(r.get("WheelChairAccessible"))) {
            pt.setWheelchairAccessible(true);
        } else if ("NOTACCESSIBLE".contentEquals(r.get("WheelChairAccessible"))) {
            pt.setWheelchairAccessible(false);
        } // Else null = unknown

        if (r.get("NumberOfCoaches") != null) {
            pt.setNumberOfCoaches(Integer.valueOf(r.get("NumberOfCoaches")));
        }

        Optional<String> quay = StopStore.getQuayFromCode(r.get("DataOwnerCode"), r.get("UserStopCode"));
        quay.ifPresent(pt::setQuayCode);

        pt.setDestinationCode(r.get("DestinationCode"));
        pt.setSideCode(r.get("SideCode"));

        pt.setGeneratedTimestamp((int) generated.toEpochSecond());
        return pt;
    }

    public static PassTime setExpected(PassTime pt) {
        if (pt.getExpectedArrivalTime() == 0 && pt.getTargetArrivalTime() != 0) {
            pt.setExpectedArrivalTime(pt.getTargetArrivalTime());
        }
        if (pt.getExpectedDepartureTime() == 0 && pt.getTargetDepartureTime() != 0) {
            pt.setExpectedDepartureTime(pt.getTargetDepartureTime());
        }
        return pt;
    }

    public static Destination toDestination(Map<String, String> r) {
        DestinationValue name = new DestinationValue(nullIfBlank(r.get("DestinationName16")),
                nullIfBlank(r.get("DestinationName19")),
                nullIfBlank(r.get("DestinationName24")),
                nullIfBlank(r.get("DestinationName50")));
        DestinationValue detail = new DestinationValue(nullIfBlank(r.get("DestinationDetail16")),
                nullIfBlank(r.get("DestinationDetail19")),
                nullIfBlank(r.get("DestinationDetail24")),
                nullIfBlank(r.get("DestinationDetail50")));
        return new Destination(r.get("DataOwnerCode")+":"+r.get("DestinationCode"), name,
                detail.isBlank() ? null : detail);
    }

    private static String nullIfBlank(String value) {
        return (value == null || value.isEmpty()) ? null : value;
    }

    public static Line toLine(Map<String, String> r) {
        return new Line(r.get("DataOwnerCode")+":"+r.get("LinePlanningNumber"), r.get("LinePublicNumber"), TransportType.valueOf(r.get("TransportType")));
    }
}
