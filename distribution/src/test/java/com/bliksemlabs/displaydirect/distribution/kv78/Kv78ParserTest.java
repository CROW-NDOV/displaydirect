package com.bliksemlabs.display_direct.common.kv78;

import nl.crowndov.displaydirect.distribution.kv78.Kv78Parser;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Packet;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class Kv78ParserTest {
    @Test
    public void parseMessage() throws Exception {
        String data = "\\GKV8turbo_passtimes|KV8turbo_passtimes|openOV RETBUS|||UTF-8|0.1|2017-04-11T20:58:42+02:00|\uFEFF\r\n" +
                "\\TDATEDPASSTIME|DATEDPASSTIME|start object\r\n" +
                "\\LDataOwnerCode|OperationDate|LinePlanningNumber|JourneyNumber|FortifyOrderNumber|UserStopOrderNumber|UserStopCode|LocalServiceLevelCode|JourneyPatternCode|LineDirection|LastUpdateTimeStamp|DestinationCode|IsTimingStop|ExpectedArrivalTime|ExpectedDepartureTime|TripStopStatus|MessageContent|MessageType|SideCode|NumberOfCoaches|WheelChairAccessible|OperatorCode|ReasonType|SubReasonType|ReasonContent|AdviceType|SubAdviceType|AdviceContent|TimingPointDataOwnerCode|TimingPointCode|JourneyStopType|TargetArrivalTime|TargetDepartureTime|RecordedArrivalTime|RecordedDepartureTime|DetectedUserStopCode|DistanceSinceDetectedUserStop|Detected_RD_X|Detected_RD_Y|VehicleNumber|BlockCode|LineVeTagNumber|VejoJourneyNumber|VehicleJourneyType|VejoBlockNumCode|JourneyModificationType|VejoDepartureTime|VejoArrivalTime|VejoTripStatusType\r\n" +
                "RET|2017-04-11|820|613894|0|28|HA1017|2950991|190863|2|2017-04-11T20:58:41+02:00|TESC---|0|20:58:27|20:58:39|ARRIVED|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|31001017|INTERMEDIATE|20:55:13|20:55:13|20:58:24|\\0|HA1017|515|92368|437750|2135|2024087|0|613894|DR|\\0|NONE|20:15:00|21:32:00|DRIVING\r\n" +
                "RET|2017-04-11|820|613894|0|29|HA1018|2950991|190863|2|2017-04-11T20:58:41+02:00|TESC---|0|20:59:36|20:59:36|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|31001018|INTERMEDIATE|20:56:11|20:56:11|\\0|\\0|HA1018|221|\\0|\\0|2135|2024087|0|613894|DR|\\0|NONE|20:15:00|21:32:00|DRIVING\r\n";

        Kv78Packet p = Kv78Parser.parseMessage(data);
        Assert.assertEquals("KV8turbo_passtimes", p.getType());
        Assert.assertEquals("openOV RETBUS", p.getComment());
        Assert.assertEquals("UTF-8", p.getEncoding());
        Assert.assertEquals("0.1", p.getVersion());
        Assert.assertEquals(ZonedDateTime.parse("2017-04-11T20:58:42+02:00"), p.getGenerated());

        Assert.assertEquals("DATEDPASSTIME", p.getTables().get(0).getTableName());
        Assert.assertEquals("start object", p.getTables().get(0).getTableComment());

        Assert.assertEquals(2, p.getTables().get(0).getRecords().size());

        Map<String, String> record = p.getTables().get(0).getRecords().get(0);
        Assert.assertEquals(38, record.size());
        Assert.assertEquals("RET", record.get("DataOwnerCode"));
        Assert.assertEquals("2017-04-11", record.get("OperationDate"));
        Assert.assertEquals("820", record.get("LinePlanningNumber"));
        Assert.assertEquals("613894", record.get("JourneyNumber"));
        Assert.assertEquals("0", record.get("FortifyOrderNumber"));
        Assert.assertEquals("28", record.get("UserStopOrderNumber"));
        Assert.assertEquals("HA1017", record.get("UserStopCode"));
        Assert.assertEquals("2950991", record.get("LocalServiceLevelCode"));
        Assert.assertEquals("190863", record.get("JourneyPatternCode"));
        Assert.assertEquals("2", record.get("LineDirection"));
        Assert.assertEquals("2017-04-11T20:58:41+02:00", record.get("LastUpdateTimeStamp"));
        Assert.assertEquals("TESC---", record.get("DestinationCode"));
        Assert.assertEquals("0", record.get("IsTimingStop"));
        Assert.assertEquals("20:58:27", record.get("ExpectedArrivalTime"));
        Assert.assertEquals("20:58:39", record.get("ExpectedDepartureTime"));
        Assert.assertEquals("ARRIVED", record.get("TripStopStatus"));
        Assert.assertEquals("-", record.get("SideCode"));
        Assert.assertEquals("1", record.get("NumberOfCoaches"));
        Assert.assertEquals("UNKNOWN", record.get("WheelChairAccessible"));
        Assert.assertEquals("ALGEMEEN", record.get("TimingPointDataOwnerCode"));
        Assert.assertEquals("31001017", record.get("TimingPointCode"));
        Assert.assertEquals("INTERMEDIATE", record.get("JourneyStopType"));
        Assert.assertEquals( "20:55:13", record.get("TargetArrivalTime"));
        Assert.assertEquals( "20:55:13", record.get("TargetDepartureTime"));
        Assert.assertEquals( "20:58:24", record.get("RecordedArrivalTime"));
        Assert.assertEquals( "HA1017", record.get("DetectedUserStopCode"));
        Assert.assertEquals( "515", record.get("DistanceSinceDetectedUserStop"));
        Assert.assertEquals( "92368", record.get("Detected_RD_X"));
        Assert.assertEquals( "437750", record.get("Detected_RD_Y"));
        Assert.assertEquals( "2135", record.get("VehicleNumber"));
        Assert.assertEquals( "0", record.get("LineVeTagNumber"));
        Assert.assertEquals( "613894", record.get("VejoJourneyNumber"));
        Assert.assertEquals( "DR", record.get("VehicleJourneyType"));
        Assert.assertEquals( "NONE", record.get("JourneyModificationType"));
        Assert.assertEquals( "20:15:00", record.get("VejoDepartureTime"));
        Assert.assertEquals( "21:32:00", record.get("VejoArrivalTime"));
        Assert.assertEquals( "DRIVING", record.get("VejoTripStatusType"));
    }

    @Test
    public void parseGeneralMessage() {
        String data = "\\GKV8turbo_generalmessages|KV8turbo_generalmessages|openOV RET|||UTF-8|0.1|2017-04-11T21:53:25+02:00|\uFEFF\r\n" +
                "\\TGENERALMESSAGEUPDATE|GENERALMESSAGEUPDATE|start object\r\n" +
                "\\LDataOwnerCode|MessageCodeDate|MessageCodeNumber|TimingPointDataOwnerCode|TimingPointCode|MessageType|MessageDurationType|MessageStartTime|MessageEndTime|MessageContent|ReasonType|SubReasonType|ReasonContent|EffectType|SubEffectType|EffectContent|MeasureType|SubMeasureType|MeasureContent|AdviceType|SubAdviceType|AdviceContent|MessageTimeStamp\r\n" +
                "RET|2017-04-11|27|ALGEMEEN|31001347|GENERAL|REMOVE|2017-04-11T21:51:08+02:00|\\0|Door defect materieel is er op tram 23 een rit uitgevallen. Houd rekening met een extra reistijd tot 15 min.|0|0|\\0|0|0|\\0|0|6|\\0|0|0|\\0|2017-04-11T21:53:24+02:00\r\n";
        Kv78Packet p = Kv78Parser.parseMessage(data);

        Assert.assertEquals("KV8turbo_generalmessages", p.getType());
        Assert.assertEquals("openOV RET", p.getComment());
        Assert.assertEquals("UTF-8", p.getEncoding());
        Assert.assertEquals("0.1", p.getVersion());
        Assert.assertEquals(ZonedDateTime.parse("2017-04-11T21:53:25+02:00"), p.getGenerated());

        Assert.assertEquals("GENERALMESSAGEUPDATE", p.getTables().get(0).getTableName());
        Assert.assertEquals("start object", p.getTables().get(0).getTableComment());

        Assert.assertEquals(1, p.getTables().get(0).getRecords().size());
        Map<String, String> record = p.getTables().get(0).getRecords().get(0);
        Assert.assertEquals(18, record.size());
        Assert.assertEquals("RET", record.get("DataOwnerCode"));
        Assert.assertEquals("2017-04-11", record.get("MessageCodeDate"));
        Assert.assertEquals("27", record.get("MessageCodeNumber"));
        Assert.assertEquals("ALGEMEEN", record.get("TimingPointDataOwnerCode"));
        Assert.assertEquals("31001347", record.get("TimingPointCode"));
        Assert.assertEquals("GENERAL", record.get("MessageType"));
        Assert.assertEquals("REMOVE", record.get("MessageDurationType"));
        Assert.assertEquals("2017-04-11T21:51:08+02:00", record.get("MessageStartTime"));
        Assert.assertEquals("Door defect materieel is er op tram 23 een rit uitgevallen. Houd rekening met een extra reistijd tot 15 min.", record.get("MessageContent"));
        Assert.assertEquals("0", record.get("ReasonType"));
        Assert.assertEquals("0", record.get("SubReasonType"));
        Assert.assertEquals("0", record.get("EffectType"));
        Assert.assertEquals("0", record.get("SubEffectType"));
        Assert.assertEquals("0", record.get("MeasureType"));
        Assert.assertEquals("6", record.get("SubMeasureType"));
        Assert.assertEquals("0", record.get("AdviceType"));
        Assert.assertEquals("0", record.get("SubAdviceType"));
        Assert.assertEquals("2017-04-11T21:53:24+02:00", record.get("MessageTimeStamp"));


    }

    @Test
    public void parseGeneralMessageDelete() {
        String data = "\\GKV8turbo_generalmessages|KV8turbo_generalmessages|openOV RET|||UTF-8|0.1|2017-04-11T22:05:46+02:00|\uFEFF\r\n" +
                "\\TGENERALMESSAGEDELETE|GENERALMESSAGEDELETE|start object\r\n" +
                "\\LDataOwnerCode|MessageCodeDate|MessageCodeNumber|TimingPointDataOwnerCode|TimingPointCode\r\n" +
                "RET|2017-04-11|27|ALGEMEEN|31001347\r\n";
        Kv78Packet p = Kv78Parser.parseMessage(data);

        Assert.assertEquals("KV8turbo_generalmessages", p.getType());
        Assert.assertEquals("openOV RET", p.getComment());
        Assert.assertEquals("UTF-8", p.getEncoding());
        Assert.assertEquals("0.1", p.getVersion());
        Assert.assertEquals(ZonedDateTime.parse("2017-04-11T22:05:46+02:00"), p.getGenerated());

        Assert.assertEquals("GENERALMESSAGEDELETE", p.getTables().get(0).getTableName());
        Assert.assertEquals("start object", p.getTables().get(0).getTableComment());

        Assert.assertEquals(1, p.getTables().get(0).getRecords().size());
        Map<String, String> record = p.getTables().get(0).getRecords().get(0);
        Assert.assertEquals(5, record.size());
        Assert.assertEquals("RET", record.get("DataOwnerCode"));
        Assert.assertEquals("2017-04-11", record.get("MessageCodeDate"));
        Assert.assertEquals("27", record.get("MessageCodeNumber"));
        Assert.assertEquals("ALGEMEEN", record.get("TimingPointDataOwnerCode"));
        Assert.assertEquals("31001347", record.get("TimingPointCode"));
    }

    @Test
    public void parsePacket() {
        String packet = "";
        try (InputStream i = Kv78ParserTest.class.getClassLoader().getResourceAsStream("kv7packet.ctx");
             InputStreamReader r = new InputStreamReader(i);
                BufferedReader bufferedReader = new BufferedReader(r)) {
            packet = bufferedReader.lines().collect(Collectors.joining("\r\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Kv78Packet p = Kv78Parser.parseMessage(packet);
        Assert.assertEquals("KV7turbo_planning", p.getType());
        Assert.assertEquals("openOV ProvFr", p.getComment());
        Assert.assertEquals("UTF-8", p.getEncoding());
        Assert.assertEquals("0.1", p.getVersion());
        Assert.assertEquals(ZonedDateTime.parse("2016-11-16T04:43:47+01:00"), p.getGenerated());

    }
}