package nl.crowndov.displaydirect.commonclient.client;


import com.google.protobuf.InvalidProtocolBufferException;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;
import nl.crowndov.displaydirect.commonclient.mqtt.MqttClient;
import nl.crowndov.displaydirect.commonclient.store.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DisplayDirectClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayDirectClient.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String clientId = null;
    private String hostname = "";
    private Future<?> receiver;
    private MqttClient client;

    private OnDisplayDirectListener listener;

    public DisplayDirectClient(String hostname) {
        this.hostname = hostname;
        this.clientId = "test_"+UUID.randomUUID().toString();
    }

    public DisplayDirectClient(String hostname, String clientId) {
        this.hostname = hostname;
        this.clientId = clientId;
    }

    public void setListener(OnDisplayDirectListener listener) {
        this.listener = listener;
    }

    private final Runnable receiveMessage = () -> {
         client = new MqttClient(hostname, clientId, DisplayConfiguration.getStopCodes(), new MqttClient.onNewMessage() {

             private List<PassTime> lastScreen = null;

             @Override
             public void onMessage(String topic, byte[] data) {
                 try {
                     if (topic.endsWith("travel_information")) {
                         DisplayDirectMessage.Container value = DisplayDirectMessage.Container.parseFrom(data);
                         DataStore.apply(DisplayDirectParser.fromContainer(value));
                         if (listener != null) {
                             listener.onMessage(value);
                             if (lastScreen != DataStore.getDepartures()) {
                                 listener.onScreenContentsChange(DataStore.getDepartures());
                                 lastScreen = DataStore.getDepartures();
                             }
                         }
                     } else if (topic.endsWith("subscription_response")) {
                         DisplayDirectMessage.SubscriptionResponse response = DisplayDirectMessage.SubscriptionResponse.parseFrom(data);
                         if (listener != null) {
                             listener.onSubscriptionResponse(response);
                         }
                     }
                 } catch (InvalidProtocolBufferException e) {
                     LOGGER.error("Failed to parse message", e);
                 }
             }
        });
    };

    public void start() {
        receiver = executor.submit(receiveMessage);
    }

    public void stop() {
        client.stop();
        receiver.cancel(true);
        executor.shutdown();
    }
}
