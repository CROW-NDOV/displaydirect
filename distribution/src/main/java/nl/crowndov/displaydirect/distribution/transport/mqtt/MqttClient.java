package nl.crowndov.displaydirect.distribution.transport.mqtt;

import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.distribution.Configuration;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MqttClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttClient.class);
    private static final String WILDCARD = "+";
    private final OnMessageListener listener;

    private MQTT mqtt;
    private final CallbackConnection connection;

    private boolean connected = false;

    public MqttClient(OnMessageListener listener) {
        this.listener = listener;
        mqtt = new MQTT();
        try {
            mqtt.setHost(Configuration.getMqttBrokerHost());
            mqtt.setCleanSession(false);
            mqtt.setClientId(Configuration.getMqttClientId());

            mqtt.setKeepAlive((short) 60);
            mqtt.setReconnectBackOffMultiplier(3);
            mqtt.setConnectAttemptsMax(10000);
            mqtt.setReconnectDelay(20);
            mqtt.setReconnectDelayMax(10000);
            mqtt.setVersion("3.1.1");

        } catch (URISyntaxException e) {
           LOGGER.error("Error setting host");
        }
        connection = mqtt.callbackConnection();
        LOGGER.info("Setting up connection");
        connection.listener(new Listener() {
            @Override
            public void onConnected() {
                connected = true;
            }

            @Override
            public void onDisconnected() {
                connected = false;
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
                // toByteArray is very important, getData DOES NOT WORK!
                listener.onMessage(topic.toString(), body.toByteArray(), ack);
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.error("An error occurred in the MQTT client", value);
            }
        });
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                LOGGER.info("Connection success");
                subscribe(TopicFactory.subscribe(null), QoS.EXACTLY_ONCE);
                subscribe(TopicFactory.unsubscribe(null), QoS.EXACTLY_ONCE);
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Connection failure");
            }
        });
    }

    private void subscribe(String topic, QoS value) {
        LOGGER.trace("Subscribing to topic '{}' with QoS {}", topic, value);
        connection.subscribe(new Topic[]{new Topic(topic, value)}, new Callback<byte[]>() {
            @Override
            public void onSuccess(byte[] value) {
                LOGGER.info("Subscribed to subscription queue {}", topic);
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Failed to subscribe to queue {}", topic);
            }
        });
    }

    public boolean send(String queue, byte[] msg, QoS quality) {
        if (!connected) {
            return false;
        }
        LOGGER.trace("Sending message to queue '{}' at Qos {}", queue, quality);
        connection.publish(queue, msg, quality, false, new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                LOGGER.trace("Succesfully sent message");
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.debug("Failed to send message", value);
            }
        });
        return true; // Or atleast assume :)
    }

    public void stop() {
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
        connection.suspend();
    }

    public interface OnMessageListener {
        void onMessage(String queue, byte[] data, Runnable ack);
    }
}
