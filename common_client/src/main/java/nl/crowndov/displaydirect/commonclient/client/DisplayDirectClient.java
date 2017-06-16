package nl.crowndov.displaydirect.commonclient.client;


import com.google.protobuf.InvalidProtocolBufferException;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;
import nl.crowndov.displaydirect.commonclient.mqtt.MqttClient;
import nl.crowndov.displaydirect.commonclient.mqtt.MqttConnection;
import nl.crowndov.displaydirect.commonclient.store.DataStore;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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
    public static final String TOPIC_TRAVEL_INFORMATION = "travel_information";
    public static final String TOPIC_SUBSCRIPTION_RESPONSE = "subscription_response";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    //private final DisplayParameters params;
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

    private MqttClient.onClientAction clientListener = new MqttClient.onClientAction() {

        private List<PassTime> lastScreen = null;

        @Override
        public void onConnect(MqttConnection connection) {
            String topic = TopicFactory.subscribe(clientId);
            LOGGER.info("Sending subscription to {}", topic);
            connection.publish(topic, createSubscriptionMessage().toByteArray(), QoS.AT_MOST_ONCE, null);
            LOGGER.info("Connection success");

            connection.subscribe(TopicFactory.travelInformation(clientId));
            connection.subscribe(TopicFactory.subscriptionResponse(clientId));
            connection.subscribe(TopicFactory.unsubscribe(clientId));
        }

        @Override
        public void onMessage(String topic, byte[] data) {
            try {
                if (topic.endsWith(TOPIC_TRAVEL_INFORMATION)) {
                    DisplayDirectMessage.Container value = DisplayDirectMessage.Container.parseFrom(data);
                    DataStore.apply(DisplayDirectParser.fromContainer(value));
                    if (listener != null) {
                        listener.onMessage(value);
                        if (lastScreen != DataStore.getDepartures()) {
                            listener.onScreenContentsChange(DataStore.getDepartures());
                            lastScreen = DataStore.getDepartures();
                        }
                    }
                } else if (topic.endsWith(TOPIC_SUBSCRIPTION_RESPONSE)) {
                    DisplayDirectMessage.SubscriptionResponse response = DisplayDirectMessage.SubscriptionResponse.parseFrom(data);
                    if (listener != null) {
                        listener.onSubscriptionResponse(response);
                    }
                }
            } catch (InvalidProtocolBufferException e) {
                LOGGER.error("Failed to parse message", e);
            }
        }
    };

    protected DisplayDirectMessage.Subscribe createSubscriptionMessage() {
        DisplayDirectMessage.Subscribe.Builder build = DisplayDirectMessage.Subscribe.newBuilder()
                .addAllStopCode(Collections.singletonList("NL:Q:50000360"))
                .setDisplayProperties(DisplayDirectMessage.Subscribe.DisplayProperties.newBuilder().setTextCharacters(0))
                .setFieldFilter(DisplayDirectMessage.Subscribe.FieldFilter.newBuilder()
                        .setTripStopStatus(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setTargetDepartureTime(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setDestination(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setLinePublicNumber(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                        .setTransportType(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS))
                .setEmail("joelhaasnoot@gmail.com")
                .setDescription("Dit is een testdisplay");

        return build.build();
    }

    private final Runnable receiveMessage = () -> {
         client = new MqttClient(hostname, clientId, DisplayConfiguration.getStopCodes(), clientListener);
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
