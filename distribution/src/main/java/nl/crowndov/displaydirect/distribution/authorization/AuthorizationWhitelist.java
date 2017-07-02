package nl.crowndov.displaydirect.distribution.authorization;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.stats.domain.LogCode;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.distribution.Configuration;
import nl.crowndov.displaydirect.distribution.authorization.domain.ValidationToken;
import nl.crowndov.displaydirect.distribution.domain.client.Subscription;
import nl.crowndov.displaydirect.distribution.domain.travelinfo.RealtimeMessage;
import nl.crowndov.displaydirect.distribution.input.QuayDataProvider;
import nl.crowndov.displaydirect.distribution.messages.DisplayDirectMessageFactory;
import nl.crowndov.displaydirect.distribution.stats.logging.Log;
import nl.crowndov.displaydirect.distribution.transport.Transport;
import nl.crowndov.displaydirect.distribution.transport.TransportFactory;
import nl.crowndov.displaydirect.distribution.util.AbstractService;
import nl.crowndov.displaydirect.distribution.util.EmailUtil;
import nl.crowndov.displaydirect.distribution.messages.SubscriptionStore;
import nl.crowndov.displaydirect.distribution.util.Store;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class AuthorizationWhitelist extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationWhitelist.class);

    private static List<String> validKeys = new ArrayList<>();
    private static Store.ValidationTokenStore validationTokens = new Store.ValidationTokenStore();

    private static Transport transport = TransportFactory.get();

    private static MessageDigest DIGEST = null;

    static {
        try {
            DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed to initialize hash algorithm", e);
        }
    }

    public static void start() {
        backup(() -> {
            readFile("authorization_keys", Store.StringList.class).ifPresent(k -> validKeys = k);
            readFile("authorization_tokens", Store.ValidationTokenStore.class).ifPresent(vt -> validationTokens = vt);
        });
    }

    public static void stop() {
        writeFile("authorization_keys", validKeys);
        writeFile("authorization_tokens", validationTokens);
    }


    public static boolean isValid(Subscription sub) {
        return validKeys.contains(getKey(sub.getId(), sub.getEmail()));
    }

    private static String getKey(String systemId, String email) {
        return systemId + "_" + email.toLowerCase();
    }

    public static Optional<ValidationToken> addValidation(String systemId, Subscription subscribe) {
        ZonedDateTime now = ZonedDateTime.now(Configuration.getZoneId());
        // Check a validation token hasn't already been handed out for this system and it's still valid
        if (validationTokens.values().stream().anyMatch(vt -> vt.getSystemId().contentEquals(systemId) && vt.getGenerated().plusHours(48).isAfter(now))) {
            return Optional.empty();
        }
        ValidationToken v = new ValidationToken(newToken(), systemId, subscribe.getEmail(), ZonedDateTime.now(ZoneId.of("UTC")), subscribe);
        validationTokens.put(v.getToken(), v);

        Map<String, Object> variables = new HashMap<>();
        variables.put("id", systemId);
        variables.put("description", subscribe.getDescription());
        variables.put("url", Configuration.getBaseUrl() + "/admin/authorize?token=" + v.getToken());
        EmailUtil.sendHtmlEmail(subscribe.getEmail(), "Rond aanmelding DisplayDirect af", "authenticate", variables);

        return Optional.ofNullable(v);
    }

    private static String newToken() {
        return getHash(UUID.randomUUID().toString());
    }

    private static String getHash(String s) {
        return DatatypeConverter.printHexBinary(DIGEST.digest(s.getBytes()));
    }

    public static boolean validate(String token) {
        if (validationTokens.containsKey(token) && validationTokens.get(token).isValid()) {
            ValidationToken vt = validationTokens.get(token);
            validKeys.add(getKey(vt.getSystemId(), vt.getEmail()));
            validationTokens.remove(token);
            // TODO: Might need to do this in a different thread, this will trigger 48 hours of messages
            // Get the planning we need // TODO: Duplicated
            List<RealtimeMessage> times = QuayDataProvider.getDataForQuay(vt.getSubscription().getSubscribedQuayCodes(), true);
            sendStatus(vt.getSystemId(), true, DisplayDirectMessage.SubscriptionResponse.Status.AUTHORISATION_VALIDATED);
            if (times.size() > 0) {
                LOGGER.debug("Got {} times to send", times.size());
                transport.sendMessage(TopicFactory.travelInformation(vt.getSystemId()), DisplayDirectMessageFactory.fromRealTime(times, vt.getSubscription()), QoS.AT_LEAST_ONCE);
                sendStatus(vt.getSystemId(), true, DisplayDirectMessage.SubscriptionResponse.Status.PLANNING_SENT);
            } else {
                sendStatus(vt.getSystemId(), true, DisplayDirectMessage.SubscriptionResponse.Status.NO_PLANNING);
            }

            SubscriptionStore.add(vt.getSubscription());
            Log.send(LogCode.AUTHORISATION_VALIDATED, vt.getSystemId(), "Authorization processed");
            Log.send(LogCode.SUBSCRIPTION_ADDED, vt.getSystemId(), "System subscribed succesfully, authorization processed");
            return true;
        }
        return false;
    }

    public static void remove(Subscription sub) {
        // aah
        validKeys.remove(getKey(sub.getId(), sub.getEmail()));
    }

    public static List<ValidationToken> getTokens() {
        return new ArrayList<>(validationTokens.values());
    }

    public static List<String> getValid() {
        return validKeys;
    }

    // TODO: Duplicate method
    private static boolean sendStatus(String id, boolean success, DisplayDirectMessage.SubscriptionResponse.Status s) {
        return transport.sendMessage(TopicFactory.subscriptionResponse(id),  DisplayDirectMessageFactory.toSubscriptionStatus(success, s), QoS.EXACTLY_ONCE);
    }
}
