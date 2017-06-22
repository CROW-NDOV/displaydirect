package nl.crowndov.displaydirect.commonclient.client;


import com.google.protobuf.InvalidProtocolBufferException;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.commonclient.configuration.AbstractConfiguration;
import nl.crowndov.displaydirect.commonclient.configuration.DisplayParameters;
import nl.crowndov.displaydirect.commonclient.configuration.SystemParameters;
import nl.crowndov.displaydirect.commonclient.domain.PassTime;
import nl.crowndov.displaydirect.commonclient.domain.SubscriptionBuilder;
import nl.crowndov.displaydirect.commonclient.mqtt.MqttClient;
import nl.crowndov.displaydirect.commonclient.mqtt.MqttConnection;
import nl.crowndov.displaydirect.commonclient.store.DataStore;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class DisplayDirectClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayDirectClient.class);
    public static final String TOPIC_TRAVEL_INFORMATION = "travel_information";
    public static final String TOPIC_SUBSCRIPTION_RESPONSE = "subscription_response";

    private DisplayParameters display;
    private SystemParameters system;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    private Future<?> receiver;
    private ScheduledFuture<?> connect;
    private MqttClient client;

    private OnDisplayDirectListener listener;

    public DisplayDirectClient(SystemParameters system, DisplayParameters display) {
        this.system = system;
        this.display = display;
        DisplayConfiguration.setDisplay(display);
        DisplayConfiguration.setSystem(system);
    }

    public DisplayDirectClient(AbstractConfiguration config) {
        this(config, config);
    }


    public void setListener(OnDisplayDirectListener listener) {
        this.listener = listener;
    }

    private MqttClient.onClientAction clientListener = new MqttClient.onClientAction() {

        private List<PassTime> lastScreen = null;

        @Override
        public void onConnect(MqttConnection connection) {
            connect = schedule.scheduleAtFixedRate(new ConnectRunnable(connection, display, system), 0, system.getSubscriptionRetryMinutes(), TimeUnit.MINUTES);

            connection.subscribe(TopicFactory.travelInformation(system.getSessionId()));
            connection.subscribe(TopicFactory.subscriptionResponse(system.getSessionId()));
            connection.subscribe(TopicFactory.unsubscribe(system.getSessionId()));
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
                    if (response.getSuccess()) {
                        connect.cancel(true);
                    }
                }
            } catch (InvalidProtocolBufferException e) {
                LOGGER.error("Failed to parse message", e);
            }
        }
    };

    public void start() {
        receiver = executor.submit( () -> client = new MqttClient(system.getConnectionString(), system.getSessionId(),
                clientListener));
    }

    public void stop() {
        client.stop();
        receiver.cancel(true);
        executor.shutdown();
        schedule.shutdown();
    }

    private static class ConnectRunnable implements Runnable {

        private final MqttConnection connection;
        private final DisplayParameters display;
        private final SystemParameters system;

        public ConnectRunnable(MqttConnection connection, DisplayParameters display, SystemParameters system) {
            this.connection = connection;
            this.display = display;
            this.system = system;
        }

        @Override
        public void run() {
            String topic = TopicFactory.subscribe(system.getSessionId());
            LOGGER.info("Sending subscription to {}", topic);
            connection.publish(topic, SubscriptionBuilder.subscribe(display, system).toByteArray(), QoS.AT_MOST_ONCE, null);
        }
    }


}
