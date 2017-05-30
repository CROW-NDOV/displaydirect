package nl.crowndov.displaydirect.distribution.messages;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.stats.domain.LogCode;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicParser;
import nl.crowndov.displaydirect.distribution.authorization.AuthorizationWhitelist;
import nl.crowndov.displaydirect.distribution.authorization.domain.ValidationToken;
import nl.crowndov.displaydirect.distribution.domain.client.Subscription;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.input.QuayDataProvider;
import nl.crowndov.displaydirect.distribution.stats.logging.Log;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class SubscriptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionHandler.class);

    private static Transport transport = TransportFactory.get();

    public static final Transport.OnMessageReceivedListener listener = (topic, data) -> {
        try {
            // TODO: Move domain logic elsewhere
            Optional<String> sender = TopicParser.getId(topic);
            Optional<String> command = TopicParser.getCommand(topic);
            if (!sender.isPresent() || !command.isPresent()) {
                LOGGER.error("Couldn't find sender or command for topic '{}'", topic);
                return true;
            }

            if (command.get().equalsIgnoreCase("subscribe")) {
                DisplayDirectMessage.Subscribe subscribe = DisplayDirectMessage.Subscribe.parser().parseFrom(data);
                Subscription sub = SubscriptionParser.toSubscription(sender.get(), subscribe);

                if (!sub.isValid()) {
                    Log.send(LogCode.SUBSCRIPTION_INVALID, sender.get(), "Authorization needed for this subscription/device, no valid email");
                    LOGGER.info("Got invalid subscription request from {}", sender.get());
                    sendStatus(sub.getId(), false, DisplayDirectMessage.SubscriptionResponse.Status.REQUEST_INVALID);
                    return true; // Can't do much about this;
                }

                if (!sub.hasValidStop()) {
                    Log.send(LogCode.SUBSCRIPTION_INVALID, sender.get(), "Stop code not valid");
                    LOGGER.info("Got invalid subscription request from {}, invalid stop {}", sender.get(),
                            sub.getSubscribedQuayCodes().stream().collect(Collectors.joining(",")));
                    sendStatus(sub.getId(), false, DisplayDirectMessage.SubscriptionResponse.Status.STOP_INVALID);
                    return true; // Can't do much about this;
                }

                if (AuthorizationWhitelist.isValid(sub)) {
                    LOGGER.debug("Got subscribe for {} from {}", sub.getSubscribedQuayCodes().stream().collect(Collectors.joining(", ")), sender.get());

                    // Get the planning we need
                    List<RealtimeMessage> times = QuayDataProvider.getDataForQuay(sub.getSubscribedQuayCodes());
                    if (times.size() > 0) {
                        LOGGER.debug("Got {} times to send", times.size());
                        transport.sendMessage(TopicFactory.travelInformation(sub.getId()), DisplayDirectMessageFactory.fromRealTime(times, sub));
                        sendStatus(sub.getId(), true, DisplayDirectMessage.SubscriptionResponse.Status.PLANNING_SENT);
                    } else {
                        sendStatus(sub.getId(), true, DisplayDirectMessage.SubscriptionResponse.Status.NO_PLANNING);
                    }

                    // Don't send messages till we've sent a planning
                    SubscriptionStore.add(sub);
                    Log.send(LogCode.SUBSCRIPTION_ADDED, sender.get(), "System subscribed succesfully, authorization already valid");
                    return true;
                } else {
                    ValidationToken t = AuthorizationWhitelist.addValidation(sender.get(), sub); // TODO: Blocking network call, queue
                    LOGGER.info("Validation required for email {}, token = {}", subscribe.getEmail(), t.getToken());
                    sendStatus(sub.getId(), false, DisplayDirectMessage.SubscriptionResponse.Status.AUTHORISATION_REQUIRED);
                    Log.send(LogCode.AUTHORISATION_SENT, sender.get(), "Authorization needed for this subscription/device");
                    return true;
                }
            } else if (command.get().equalsIgnoreCase("unsubscribe")) {
                LOGGER.debug("Got unsubscribe from {}", sender.get());
                DisplayDirectMessage.Unsubscribe unsubscribe = DisplayDirectMessage.Unsubscribe.parser().parseFrom(data);
                // TODO: check if it doesn't exist waiting for authentication
                if (SubscriptionStore.isSystemSubscribed(sender.get())) {
                    if (unsubscribe.getIsPermanent()) {
                        SubscriptionStore.getForSystem(sender.get()).ifPresent(AuthorizationWhitelist::remove);
                        SubscriptionStore.remove(sender.get());
                        Log.send(LogCode.SUBSCRIPTION_REMOVED, sender.get(), "System unsubscribed permanently");
                    } else {
                        SubscriptionStore.remove(sender.get());
                        Log.send(LogCode.SUBSCRIPTION_REMOVED, sender.get(), "System disconnected temporarily");
                    }
                } else {
                    Log.send(LogCode.SUBSCRIPTION_INVALID_UNSUBSCRIBE, sender.get(), "System wasn't subscribed, but got unsubscribe");
                }
                return true;
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Failed to parse message", e);
            return true; // Can't do much about this
        }
        return false;
    };

    private static boolean sendStatus(String id, boolean success, DisplayDirectMessage.SubscriptionResponse.Status s) {
        return transport.sendMessage(TopicFactory.subscriptionResponse(id),  DisplayDirectMessageFactory.toSubscriptionStatus(success, s));
    }
}
