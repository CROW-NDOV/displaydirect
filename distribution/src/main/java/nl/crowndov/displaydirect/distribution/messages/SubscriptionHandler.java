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
import org.fusesource.mqtt.client.QoS;
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
            Optional<String> sender = TopicParser.getId(topic);
            Optional<String> command = TopicParser.getCommand(topic);
            if (!sender.isPresent() || !command.isPresent()) {
                LOGGER.error("Couldn't find sender or command for topic '{}'", topic);
                return true;
            }

            if (command.get().equalsIgnoreCase("subscribe")) {
                return handleSubscribe(data, sender.get());
            } else if (command.get().equalsIgnoreCase("unsubscribe")) {
                return handleUnsubscribe(data, sender.get());
            }
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Failed to parse message", e);
            return true; // Can't do much about this
        }
        return false;
    };

    private static boolean handleSubscribe(byte[] data, String sender) throws InvalidProtocolBufferException {
        DisplayDirectMessage.Subscribe subscribe = DisplayDirectMessage.Subscribe.parser().parseFrom(data);
        Subscription sub = SubscriptionParser.toSubscription(sender, subscribe);

        if (!sub.isValid()) {
            Log.send(LogCode.SUBSCRIPTION_INVALID, sender, "Authorization needed for this subscription/device, no valid email");
            LOGGER.info("Got invalid subscription request from {}", sender);
            sendStatus(sub.getId(), false, DisplayDirectMessage.SubscriptionResponse.Status.REQUEST_INVALID);
            return true; // Can't do much about this;
        }

        if (!sub.hasValidStop()) {
            Log.send(LogCode.SUBSCRIPTION_INVALID, sender, "Stop code not valid");
            LOGGER.info("Got invalid subscription request from {}, invalid stop {}", sender,
                    sub.getSubscribedQuayCodes().stream().collect(Collectors.joining(",")));
            sendStatus(sub.getId(), false, DisplayDirectMessage.SubscriptionResponse.Status.STOP_INVALID);
            return true; // Can't do much about this;
        }

        if (AuthorizationWhitelist.isValid(sub)) {
            LOGGER.debug("Got subscribe for {} from {}", sub.getSubscribedQuayCodes().stream().collect(Collectors.joining(", ")), sender);

            // If already subscribed, remove system before sending planning and adding it.
            if (SubscriptionStore.isSystemSubscribed(sub.getId())) {
                SubscriptionStore.remove(sub.getId());
            }

            // Get the planning we need
            List<RealtimeMessage> times = QuayDataProvider.getDataForQuay(sub.getSubscribedQuayCodes(), true);
            if (times.size() > 0) {
                LOGGER.debug("Got {} times to send", times.size());
                transport.sendMessage(TopicFactory.travelInformation(sub.getId()), DisplayDirectMessageFactory.fromRealTime(times, sub), QoS.AT_LEAST_ONCE);
                sendStatus(sub.getId(), true, DisplayDirectMessage.SubscriptionResponse.Status.PLANNING_SENT);
            } else {
                sendStatus(sub.getId(), true, DisplayDirectMessage.SubscriptionResponse.Status.NO_PLANNING);
            }

            // Don't send messages till we've sent a planning
            SubscriptionStore.add(sub);
            Log.send(LogCode.SUBSCRIPTION_ADDED, sender, "System subscribed succesfully, authorization already valid");
            return true;
        } else {
            // TODO: Blocking network call, queue and error handling
            ValidationToken t = AuthorizationWhitelist.addValidation(sender, sub);
            LOGGER.info("Validation required for email {}, token = {}", subscribe.getEmail(), t.getToken());
            sendStatus(sub.getId(), false, DisplayDirectMessage.SubscriptionResponse.Status.AUTHORISATION_REQUIRED);
            Log.send(LogCode.AUTHORISATION_SENT, sender, "Authorization needed for this subscription/device");
            return true;
        }
    }

    private static boolean handleUnsubscribe(byte[] data, String sender) throws InvalidProtocolBufferException {
        LOGGER.debug("Got unsubscribe from {}", sender);
        DisplayDirectMessage.Unsubscribe unsubscribe = DisplayDirectMessage.Unsubscribe.parser().parseFrom(data);
        // TODO: check if it doesn't exist waiting for authentication
        if (SubscriptionStore.isSystemSubscribed(sender)) {
            SubscriptionStore.remove(sender);
            String mode = "temporarily";
            if (unsubscribe.getIsPermanent()) {
                SubscriptionStore.getForSystem(sender).ifPresent(AuthorizationWhitelist::remove);
                mode = "permanently";
            }
            Log.send(LogCode.SUBSCRIPTION_REMOVED, sender, "System unsubscribed "+mode);
        } else {
            Log.send(LogCode.SUBSCRIPTION_INVALID_UNSUBSCRIBE, sender, "System wasn't subscribed, but got unsubscribe");
        }
        return true;
    }

    private static boolean sendStatus(String id, boolean success, DisplayDirectMessage.SubscriptionResponse.Status s) {
        return transport.sendMessage(TopicFactory.subscriptionResponse(id),  DisplayDirectMessageFactory.toSubscriptionStatus(success, s), QoS.EXACTLY_ONCE);
    }
}
