package com.bliksemlabs.display_direct.common.kv78;

import com.netflix.config.ConfigurationManager;
import nl.crowndov.displaydirect.distribution.input.InputHandler;
import nl.crowndov.displaydirect.distribution.kv78.Kv78Parser;
import nl.crowndov.displaydirect.distribution.kv78.domain.Kv78Packet;
import nl.crowndov.displaydirect.distribution.transport.TestTransport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public void parseNullPointer() {
        ConfigurationManager.getConfigInstance().setProperty("distribution.transport", "test");
        String data = "\\GKV8turbo_passtimes|KV8turbo_passtimes|openOV UT-STD|||UTF-8|0.1|2017-06-05T10:33:33+02:00|\uFEFF\n" +
                "\\TDATEDPASSTIME|DATEDPASSTIME|start object\n" +
                "\\LDataOwnerCode|OperationDate|LinePlanningNumber|JourneyNumber|FortifyOrderNumber|UserStopOrderNumber|UserStopCode|LocalServiceLevelCode|JourneyPatternCode|LineDirection|LastUpdateTimeStamp|DestinationCode|IsTimingStop|ExpectedArrivalTime|ExpectedDepartureTime|TripStopStatus|MessageContent|MessageType|SideCode|NumberOfCoaches|WheelChairAccessible|OperatorCode|ReasonType|SubReasonType|ReasonContent|AdviceType|SubAdviceType|AdviceContent|TimingPointDataOwnerCode|TimingPointCode|JourneyStopType|TargetArrivalTime|TargetDepartureTime|RecordedArrivalTime|RecordedDepartureTime|DetectedUserStopCode|DistanceSinceDetectedUserStop|Detected_RD_X|Detected_RD_Y|VehicleNumber|BlockCode|LineVeTagNumber|VejoJourneyNumber|VehicleJourneyType|VejoBlockNumCode|JourneyModificationType|VejoDepartureTime|VejoArrivalTime|VejoTripStatusType\n" +
                "QBUZZ|2017-06-05|u003|7019|0|1|50003005|3023726|171755|1|2017-06-05T10:33:31+02:00|2131|1|10:49:00|10:49:00|ARRIVED|\\0|\\0|G5|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50003005|FIRST|10:49:00|10:49:00|10:28:36|\\0|50003005|0|\\0|\\0|4103|600321|3|7019|DR|\\0|NONE|10:49:00|11:33:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u127|7004|0|21|51111541|3023916|171827|2|2017-06-05T10:33:32+02:00|2284|0|10:40:56|10:40:56|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|51111541|INTERMEDIATE|10:42:00|10:42:00|\\0|\\0|51111541|1398|\\0|\\0|4351|610903|127|7004|DR|\\0|NONE|10:16:00|10:45:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u127|7004|0|20|51114701|3023916|171827|2|2017-06-05T10:33:32+02:00|2284|0|10:36:32|10:36:32|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|51114701|INTERMEDIATE|10:38:00|10:38:00|\\0|\\0|51114701|2296|\\0|\\0|4351|610903|127|7004|DR|\\0|NONE|10:16:00|10:45:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u127|7004|0|22|51115112|3023916|171827|2|2017-06-05T10:33:32+02:00|2284|0|10:42:02|10:42:02|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|51115112|INTERMEDIATE|10:43:00|10:43:00|\\0|\\0|51115112|443|\\0|\\0|4351|610903|127|7004|DR|\\0|NONE|10:16:00|10:45:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u127|7004|0|19|51200500|3023916|171827|2|2017-06-05T10:33:32+02:00|2284|1|10:33:49|10:35:00|PASSED|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|51200500|INTERMEDIATE|10:35:00|10:35:00|\\0|\\0|51200500|939|129015|457197|4351|610903|127|7004|DR|\\0|NONE|10:16:00|10:45:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u041|7015|0|22|50190020|3023843|165466|1|2017-06-05T10:33:31+02:00|2422|0|10:38:56|10:38:56|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50190020|INTERMEDIATE|10:39:00|10:39:00|\\0|\\0|50190020|905|\\0|\\0|4464|620712|41|7015|DR|\\0|NONE|10:11:00|10:55:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u041|7015|0|21|50190040|3023843|165466|1|2017-06-05T10:33:31+02:00|2422|0|10:37:50|10:37:50|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50190040|INTERMEDIATE|10:38:00|10:38:00|\\0|\\0|50190040|583|\\0|\\0|4464|620712|41|7015|DR|\\0|NONE|10:11:00|10:55:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u041|7015|0|20|50190060|3023843|165466|1|2017-06-05T10:33:31+02:00|2422|0|10:36:44|10:36:44|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50190060|INTERMEDIATE|10:37:00|10:37:00|\\0|\\0|50190060|1009|\\0|\\0|4464|620712|41|7015|DR|\\0|NONE|10:11:00|10:55:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u041|7015|0|19|50190080|3023843|165466|1|2017-06-05T10:33:31+02:00|2422|0|10:34:32|10:34:32|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50190080|INTERMEDIATE|10:35:00|10:35:00|\\0|\\0|50190080|800|\\0|\\0|4464|620712|41|7015|DR|\\0|NONE|10:11:00|10:55:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u041|7015|0|23|50190100|3023843|165466|1|2017-06-05T10:33:31+02:00|2422|0|10:40:00|10:40:00|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50190100|INTERMEDIATE|10:40:00|10:40:00|\\0|\\0|50190100|796|\\0|\\0|4464|620712|41|7015|DR|\\0|NONE|10:11:00|10:55:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u041|7015|0|18|50290040|3023843|165466|1|2017-06-05T10:33:31+02:00|2422|0|10:33:27|10:33:27|PASSED|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50290040|INTERMEDIATE|10:34:00|10:34:00|\\0|10:33:31|50290040|623|142348|453114|4464|620712|41|7015|DR|\\0|NONE|10:11:00|10:55:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u077|7009|0|37|50000350|3023901|165480|1|2017-06-05T10:33:32+02:00|2010|0|10:35:27|10:35:27|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000350|INTERMEDIATE|10:35:00|10:35:00|\\0|\\0|50000350|315|\\0|\\0|4515|620811|77|7009|DR|\\0|NONE|09:48:00|10:58:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u077|7009|0|35|50000360|3023901|165480|1|2017-06-05T10:33:32+02:00|2010|0|10:33:39|10:33:39|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000360|INTERMEDIATE|10:33:00|10:33:00|\\0|\\0|50000360|1293|\\0|\\0|4515|620811|77|7009|DR|\\0|NONE|09:48:00|10:58:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u077|7009|0|36|50000370|3023901|165480|1|2017-06-05T10:33:32+02:00|2010|0|10:34:33|10:34:33|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000370|INTERMEDIATE|10:34:00|10:34:00|\\0|\\0|50000370|345|\\0|\\0|4515|620811|77|7009|DR|\\0|NONE|09:48:00|10:58:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u077|7009|0|38|50000630|3023901|165480|1|2017-06-05T10:33:32+02:00|2010|0|10:36:21|10:36:21|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000630|INTERMEDIATE|10:36:00|10:36:00|\\0|\\0|50000630|466|\\0|\\0|4515|620811|77|7009|DR|\\0|NONE|09:48:00|10:58:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u077|7009|0|39|50000650|3023901|165480|1|2017-06-05T10:33:32+02:00|2010|0|10:37:15|10:37:15|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000650|INTERMEDIATE|10:37:00|10:37:00|\\0|\\0|50000650|406|\\0|\\0|4515|620811|77|7009|DR|\\0|NONE|09:48:00|10:58:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u077|7009|0|40|50000710|3023901|165480|1|2017-06-05T10:33:32+02:00|2010|0|10:39:03|10:39:03|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000710|INTERMEDIATE|10:39:00|10:39:00|\\0|\\0|50000710|1067|\\0|\\0|4515|620811|77|7009|DR|\\0|NONE|09:48:00|10:58:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u077|7012|0|1|50220040|3023901|165477|2|2017-06-05T10:33:32+02:00|2112|1|10:34:00|10:34:00|ARRIVED|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|90000345|FIRST|10:34:00|10:34:00|10:27:45|\\0|50220040|0|\\0|\\0|4485|620817|77|7012|DR|\\0|NONE|10:34:00|11:45:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|9|50000640|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:38:13|10:38:13|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000640|INTERMEDIATE|10:38:00|10:38:00|\\0|\\0|50000640|421|\\0|\\0|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|8|50000660|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:37:19|10:37:19|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50000660|INTERMEDIATE|10:37:00|10:37:00|\\0|\\0|50000660|1202|\\0|\\0|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|13|50003340|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:42:00|10:42:00|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50003340|INTERMEDIATE|10:42:00|10:42:00|\\0|\\0|50003340|223|\\0|\\0|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|10|50003350|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:39:07|10:39:07|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50003350|INTERMEDIATE|10:39:00|10:39:00|\\0|\\0|50003350|467|\\0|\\0|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|12|50003360|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:41:00|10:41:00|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50003360|INTERMEDIATE|10:41:00|10:41:00|\\0|\\0|50003360|325|\\0|\\0|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|11|50003370|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:40:01|10:40:01|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50003370|INTERMEDIATE|10:40:00|10:40:00|\\0|\\0|50003370|319|\\0|\\0|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|7|50100420|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:35:31|10:35:31|DRIVING|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50100420|INTERMEDIATE|10:35:00|10:35:00|\\0|\\0|50100420|758|\\0|\\0|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n" +
                "QBUZZ|2017-06-05|u028|7018|0|6|50100440|3023802|171804|2|2017-06-05T10:33:32+02:00|2194|0|10:34:09|10:34:09|PASSED|\\0|\\0|-|1|UNKNOWN|\\0|\\0|\\0|\\0|\\0|\\0|\\0|ALGEMEEN|50100440|INTERMEDIATE|10:33:00|10:33:00|\\0|\\0|50100440|850|139249|455533|4145|600313|28|7018|DR|\\0|NONE|10:27:00|11:14:00|DRIVING\n";
        ((TestTransport) TransportFactory.get()).registerListener((topic, data1) -> {
            Assert.assertNotNull(data1);
        });
        InputHandler.handleMessage(data);

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