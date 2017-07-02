package nl.crowndov.displaydirect.commonclient.mqtt;

import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.commonclient.domain.SubscriptionBuilder;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * Copyright 2017 CROW-NDOV
 * <p>
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MqttClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttClient.class);

    private MQTT mqtt;
    private final CallbackConnection connection;
    private final byte[] disconnect = SubscriptionBuilder.unsubscribe().toByteArray();
    private final String disconnectTopic;

    public MqttClient(String hostname, String clientId, onClientAction msg) {
        mqtt = new MQTT();
        try {
            mqtt.setHost(hostname);
        } catch (URISyntaxException e) {
            LOGGER.error("Error setting host");
        }
        mqtt.setClientId(clientId);
        mqtt.setCleanSession(true);
        mqtt.setKeepAlive((short) 90);
        mqtt.setWillMessage(new UTF8Buffer(disconnect));
        disconnectTopic = TopicFactory.unsubscribe(clientId);
        mqtt.setWillTopic(disconnectTopic);
        mqtt.setWillQos(QoS.EXACTLY_ONCE);
        mqtt.setVersion("3.1.1");

        connection = mqtt.callbackConnection();
        MqttConnection connect = new MqttConnection(connection);
        LOGGER.info("Setting up connection");
        connection.listener(new Listener() {
            @Override
            public void onConnected() {
                LOGGER.info("Connected");
            }

            @Override
            public void onDisconnected() {
                LOGGER.info("Disconnected");
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                LOGGER.trace("Got message on topic {}", topic.toString());
                msg.onMessage(topic.toString(), body.toByteArray());
                ack.run();
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("MQTT Client failed", value);
            }
        });
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                msg.onConnect(connect);
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Connection failure");
            }
        });

    }


    public void stop(boolean unsubscribe) {
        if (unsubscribe) { // Try to unsubscribe
            // TODO: This fails with a QoS of EXACTLY_ONCE, so it never arrives
            connection.publish(disconnectTopic, disconnect, QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {
                    LOGGER.info("Successfully sent disconnect");
                }

                @Override
                public void onFailure(Throwable value) {
                    LOGGER.info("Failed to send disconnect", value);
                }
            });
        }
        connection.disconnect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                LOGGER.info("Disconnect success");
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Disconnect failure", value);
            }
        });
    }

    public interface onClientAction {
        void onConnect(MqttConnection connection);

        void onMessage(String topic, byte[] data);
    }
}
