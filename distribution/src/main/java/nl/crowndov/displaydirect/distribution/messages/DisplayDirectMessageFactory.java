package nl.crowndov.displaydirect.distribution.messages;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.stats.domain.LogMessage;
import nl.crowndov.displaydirect.common.stats.domain.Metric;
import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.domain.client.Subscription;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.*;
import nl.crowndov.displaydirect.distribution.input.Kv78ProcessTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage.Type.MESSAGE_DELETE;
import static nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage.Type.MESSAGE_UPDATE;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DisplayDirectMessageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(Kv78ProcessTask.class);

    public static byte[] fromRealTime(List<RealtimeMessage> messages, Subscription config) {
        if (messages.size() == 0) {
            return null;
        }

        DisplayDirectMessage.PassingTimes.Builder passingTimes = passTime(config, messages.stream()
                .filter(m -> m.getType() == RealtimeMessage.Type.PASSTIME)
                .collect(Collectors.toList()));
        DisplayDirectMessage.GeneralMessage.Builder generalMessages = generalMessage(messages.stream()
                .filter(m -> m.getType() != RealtimeMessage.Type.PASSTIME)
                .collect(Collectors.toList()));

        return DisplayDirectMessage.Container.newBuilder()
                .addPassingTimes(passingTimes)
                .addGeneralMessages(generalMessages)
                .build()
                .toByteArray();
    }


    private static DisplayDirectMessage.PassingTimes.Builder passTime(Subscription config, List<RealtimeMessage> messages) {
        DisplayDirectMessage.PassingTimes.Builder b = DisplayDirectMessage.PassingTimes.newBuilder();

        for (RealtimeMessage m : messages) {
            PassTime message = (PassTime) m;
            b = b.addPassTimeHash(message.getPassTimeHash());

            if (config.getExpectedArrivalTime() != Subscription.FieldDelivery.NEVER){
                if (message.getExpectedArrivalTime() != 0) {
                    b = b.addExpectedArrivalTime(message.getExpectedArrivalTime());
                } else {
                    b = b.addExpectedArrivalTime(-1);
                }
            }

            if (message.getExpectedDepartureTime() != 0) {
                b = b.addExpectedDepartureTime(message.getExpectedDepartureTime());
            } else {
                b = b.addExpectedDepartureTime(-1);
            }

            if (config.getTargetArrivalTime() != Subscription.FieldDelivery.NEVER) {
                if (message.getTargetArrivalTime() != 0) {
                    b = b.addTargetArrivalTime(message.getTargetArrivalTime());
                } else {
                    b = b.addTargetArrivalTime(-1);
                }
            }

            if (config.getTargetDepartureTime() != Subscription.FieldDelivery.NEVER) {
                if (message.getTargetDepartureTime() != 0) {
                    b = b.addTargetDepartureTime(message.getTargetDepartureTime());
                } else {
                    b = b.addTargetDepartureTime(-1);
                }
            }

            if (config.getNumberOfCoaches() != Subscription.FieldDelivery.NEVER) {
                b = b.addNumberOfCoaches(message.getNumberOfCoaches());
            }

            if (config.getTripStopStatus() != Subscription.FieldDelivery.NEVER) {
                b = b.addTripStopStatus(translateTripStopStatus(message.getTripStopStatus()));
            }

            if (config.getWheelchairAccessible() != Subscription.FieldDelivery.NEVER) {
                if (message.getWheelchairAccessible() != null) {
                    b = b.addWheelchairAccessible(message.getWheelchairAccessible());
                } else {
                    b = b.addWheelchairAccessible(false);
                }
            }

            if (config.getIsTimingStop() != Subscription.FieldDelivery.NEVER) {
                if (message.getIsTimingStop() != null) {
                    b = b.addIsTimingStop(message.getIsTimingStop());
                } else {
                    b = b.addIsTimingStop(false);
                }
            }

            if (config.getLinePublicNumber() != Subscription.FieldDelivery.NEVER) {
                if (message.getLine() != null) {
                    b = b.addLinePublicNumber(message.getLine().getPublicNumber());
                } else {
                    b = b.addLinePublicNumber("");
                }
            }

            if (config.getTransportType() != Subscription.FieldDelivery.NEVER) {
                if (message.getLine() != null) {
                    b = b.addTransportType(getTransportType(message.getLine().getType()));
                } else {
                    //b = b.addTransportType(null); // TODO: this needs to be an unknwon
                }
            }

            if (config.getDestination() != Subscription.FieldDelivery.NEVER) {
                DisplayDirectMessage.PassingTimes.Destination.Builder dest = DisplayDirectMessage.PassingTimes.Destination.newBuilder();
                if (message.getDestination() != null) {
                    dest = dest.addValue(message.getDestination().getBestDestinationName(config.getTextCharacters()));
                    if (message.getDestination().hasDetail()) {
                        String destDetail = message.getDestination().getBestDestinationDetail(config.getTextCharacters());
                        if (destDetail != null) {
                            dest = dest.addValue(destDetail);
                        }
                    }
                }
                b = b.addDestination(dest.build());
            }

            if (config.getStopCode() != Subscription.FieldDelivery.NEVER) {
                if (message.getStopCode() != null) {
                    b = b.addStopCode(message.getStopCode());
                } else {
                    b = b.addStopCode("");
                }
            }

            if (config.getSideCode() != Subscription.FieldDelivery.NEVER) {
                if (message.getSideCode() != null) {
                    b = b.addSideCode(message.getSideCode());
                } else {
                    b = b.addSideCode("");
                }
            }

            if (config.getLineDirection() != Subscription.FieldDelivery.NEVER) {
                if (message.getLineDirection() != -1) {
                    b = b.addLineDirection(message.getLineDirection());
                } else {
                    b = b.addLineDirection(-1);
                }
            }

            if (config.getGeneratedTimestamp() != null) {
                if (message.getGeneratedTimestamp() != -1) {
                    b.addGeneratedTimestamp(message.getGeneratedTimestamp());
                } else {
                    b.addGeneratedTimestamp(-1);
                }
            }

            // TODO: Line color, line text color, line icon, direction color, direction text color, direction icon

            if (config.getJourneyMessageContent() != Subscription.FieldDelivery.NEVER) {
                // TODO: do something
            }
        }

        return b;
    }

    private static DisplayDirectMessage.PassingTimes.TransportType getTransportType(TransportType type) {
        switch(type) {
            case TRAM:
                return DisplayDirectMessage.PassingTimes.TransportType.TRAM;
            case TRAIN:
                return DisplayDirectMessage.PassingTimes.TransportType.TRAIN;
            case METRO:
                return DisplayDirectMessage.PassingTimes.TransportType.METRO;
            case BOAT:
                return DisplayDirectMessage.PassingTimes.TransportType.BOAT;
            case BUS:
            default:
                return DisplayDirectMessage.PassingTimes.TransportType.BUS;
        }
    }

    private static DisplayDirectMessage.PassingTimes.TripStopStatus translateTripStopStatus(PassTime.Status TripStopStatus) {
        switch (TripStopStatus) {
            case PLANNED:
                return DisplayDirectMessage.PassingTimes.TripStopStatus.PLANNED;
            case DRIVING:
                return DisplayDirectMessage.PassingTimes.TripStopStatus.DRIVING;
            case CANCEL:
                return DisplayDirectMessage.PassingTimes.TripStopStatus.CANCELLED;
            case PASSED:
                return DisplayDirectMessage.PassingTimes.TripStopStatus.PASSED;
            case ARRIVED:
                return DisplayDirectMessage.PassingTimes.TripStopStatus.ARRIVED;
            case UNKNOWN:
                return DisplayDirectMessage.PassingTimes.TripStopStatus.UNKNOWN;
            default: // Oops ;)
                return DisplayDirectMessage.PassingTimes.TripStopStatus.DELETED;
        }
    }

    private static DisplayDirectMessage.GeneralMessage.Builder generalMessage(List<RealtimeMessage> messages) {
        DisplayDirectMessage.GeneralMessage.Builder b = DisplayDirectMessage.GeneralMessage.newBuilder();

        for (RealtimeMessage m : messages) {
            if (m.getType() == MESSAGE_UPDATE) {
                UpdateMessage message = (UpdateMessage) m;
                b = b.addMessageHash(message.getMessageHash())
                        .addMessageContent(message.getMessageContent())
                        .addMessageStartTime((int) message.getMessageStart().toEpochSecond());

                if (message.getMessageEnd() != null) {
                    b = b.addMessageEndTime((int) message.getMessageEnd().toEpochSecond());
                } else {
                    b = b.addMessageEndTime(0);
                }
            } else if (m.getType() == MESSAGE_DELETE){
                DeleteMessage message = (DeleteMessage) m;
                b = b.addMessageHash(message.getMessageHash())
                        .addMessageContent("")
                        .addMessageStartTime(0)
                        .addMessageEndTime(0);
            }
        }

        return b;
    }

    public static byte[] toMonitoringMetrics(List<Metric> input) {
        List<DisplayDirectMessage.Monitoring.Metric> metrics = input.stream()
                .map(m -> DisplayDirectMessage.Monitoring.Metric.newBuilder()
                        .setName(m.getName())
                        .setValue(Math.toIntExact(m.getValue()))
                        .setTimestamp((int) m.getCreated().toEpochSecond(ZoneOffset.UTC))
                        .build())
                .collect(Collectors.toList());
        return DisplayDirectMessage.Monitoring.newBuilder().addAllMetrics(metrics).build().toByteArray();
    }

    public static byte[] toMonitoringLogs(List<LogMessage> input) {
        List<DisplayDirectMessage.Monitoring.LogMessage> metrics = input.stream()
                .map(DisplayDirectMessageFactory::toLogMessage)
                .collect(Collectors.toList());
        return DisplayDirectMessage.Monitoring.newBuilder().addAllLogs(metrics).build().toByteArray();
    }

    public static byte[] toMonitoringLog(LogMessage input) {
        DisplayDirectMessage.Monitoring.LogMessage l = toLogMessage(input);
        return DisplayDirectMessage.Monitoring.newBuilder().addLogs(l).build().toByteArray();
    }

    private static DisplayDirectMessage.Monitoring.LogMessage toLogMessage(LogMessage l) {
        DisplayDirectMessage.Monitoring.LogMessage.Builder b = DisplayDirectMessage.Monitoring.LogMessage.newBuilder()
                .setCode(l.getCode().getValue())
                .setMessage(l.getMessage())
                .setTimestamp((int) l.getTimestamp().toEpochSecond(ZoneOffset.UTC));
        if (l.getStopSystemId() != null) {
            b = b.setStopsystemId(l.getStopSystemId());
        }
        return b.build();
    }

    public static byte[] toSubscriptionStatus(boolean isSuccesful, DisplayDirectMessage.SubscriptionResponse.Status status) {
        return DisplayDirectMessage.SubscriptionResponse.newBuilder()
                .setSuccess(isSuccesful)
                .setStatus(status)
                .setTimestamp((int) ZonedDateTime.now(Configuration.getZoneId()).toEpochSecond())
                .build()
                .toByteArray();
    }
}
