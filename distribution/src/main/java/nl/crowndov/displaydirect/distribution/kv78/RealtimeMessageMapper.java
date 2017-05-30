package nl.crowndov.displaydirect.distribution.kv78;

import nl.crowndov.displaydirect.distribution.domain.travelinfo.DeleteMessage;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.PassTime;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.UpdateMessage;
import nl.crowndov.displaydirect.distribution.input.TimingPointProvider;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Packet;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Table;
import nl.crowndov.displaydirect.distribution.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class RealtimeMessageMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeMessageMapper.class);

    public static List<RealtimeMessage> toRealtimeMessage(Kv78Packet packet) {
        if (packet.getTables().size() != 1) {
            return new ArrayList<>();
        }
        Kv78Table t = packet.getTables().get(0);
        if (t != null && t.getTableName() != null) {
            switch (t.getTableName()) {
                case "DATEDPASSTIME":
                    return handle(packet, t.getRecords(), RealtimeMessageMapper::passTime);
                case "GENERALMESSAGEUPDATE":
                    LOGGER.info("Got general message update");
                    return handle(packet, t.getRecords(), RealtimeMessageMapper::updateMessage);
                case "GENERALMESSAGEDELETE":
                    LOGGER.info("Got general message delete");
                    return handle(packet, t.getRecords(), RealtimeMessageMapper::deleteMessage);
                default:
                    break;
            }
        }
        return new ArrayList<>();
    }

    private static List<RealtimeMessage> handle(Kv78Packet packet, List<Map<String, String>> records, BiFunction<Kv78Packet, Map<String, String>, RealtimeMessage> f) {
        return records.stream().map(r -> f.apply(packet, r)).collect(Collectors.toList());
    }

    public static RealtimeMessage passTime(Kv78Packet packet, Map<String, String> r) {
        LocalDate date = LocalDate.parse(r.get("OperationDate"));
        PassTime pt = new PassTime(r.get("DataOwnerCode"), date,
                r.get("LinePlanningNumber"), Integer.valueOf(r.get("JourneyNumber")), r.get("UserStopCode"));
        pt.setExpectedArrivalTime((int) DateTimeUtil.getTime(date, r.get("ExpectedArrivalTime")).toEpochSecond());
        pt.setExpectedDepartureTime((int) DateTimeUtil.getTime(date, r.get("ExpectedDepartureTime")).toEpochSecond());
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

        pt.setTripStopStatus(PassTime.Status.valueOf(r.get("TripStopStatus")));
        pt.setDestinationCode(r.get("DestinationCode"));
        pt.setSideCode(r.get("SideCode"));

        pt.setTimingStop("1".contentEquals(r.get("IsTimingStop")));
        pt.setLineDirection(Integer.valueOf(r.get("LineDirection")));

        pt.setGeneratedTimestamp((int) packet.getGenerated().toEpochSecond());
        return pt;
    }

    private static RealtimeMessage updateMessage(Kv78Packet packet, Map<String, String> r) {
        // Find a matching user stop code
        Optional<String> stopCode = TimingPointProvider.getStopFromTimingPoint(r.get("TimingPointDataOwnerCode"), r.get("TimingPointCode"));
        if (!stopCode.isPresent()) {
            return null;
        }
        String[] split = stopCode.get().split("\\|");

        UpdateMessage update = new UpdateMessage(split[0], LocalDate.parse(r.get("MessageCodeDate")),
                Integer.valueOf(r.get("MessageCodeNumber")), split[1]);
        update.setMessageContent(r.get("MessageContent"));
        if (r.containsKey("MessageStartTime")) {
            update.setMessageStart(ZonedDateTime.parse(r.get("MessageStartTime")));
        }
        if (r.containsKey("MessageEndTime")) {
            update.setMessageEnd(ZonedDateTime.parse(r.get("MessageEndTime")));
        }

        return update;
    }

    private static RealtimeMessage deleteMessage(Kv78Packet packet, Map<String, String> r) {
        return new DeleteMessage(r.get("DataOwnerCode"), LocalDate.parse(r.get("MessageCodeDate")),
                Integer.valueOf(r.get("MessageCodeNumber")), r.get("TimingPointCode")); //r.get("TimingPointDataOwnerCode")
    }

}
