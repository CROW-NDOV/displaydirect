package nl.crowndov.displaydirect.dashboard.mqtt;

import com.google.protobuf.InvalidProtocolBufferException;
import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.dashboard.Configuration;
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

    private MQTT mqtt;
    private final CallbackConnection connection;

    private Callback<byte[]> subscriptionCompleteCallback = new Callback<byte[]>() {
        @Override
        public void onSuccess(byte[] value) {
            LOGGER.info("Subscribed succesfully");
        }

        @Override
        public void onFailure(Throwable value) {
            LOGGER.info("Error during subscribing", value);
        }
    };

    public MqttClient(Callback<DisplayDirectMessage.Monitoring> msg) {
        mqtt = new MQTT();
        try {
            mqtt.setHost(Configuration.getHostname());
            mqtt.setClientId(Configuration.getClientId());
            mqtt.setCleanSession(false);
            mqtt.setKeepAlive((short) 10);
            mqtt.setVersion("3.1.1");
        } catch (URISyntaxException e) {
           LOGGER.error("Error setting host");
        }
        connection = mqtt.callbackConnection();
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
                try {
                    DisplayDirectMessage.Monitoring mon = DisplayDirectMessage.Monitoring.parseFrom(body.toByteArray());
                    msg.onSuccess(mon);
                } catch (InvalidProtocolBufferException e) {
                    LOGGER.error("Failed to parse message", e);
                    msg.onFailure(e);
                }
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
                LOGGER.info("Subscribing to topics");
                Topic[] topics = { new Topic (TopicFactory.monitoring(), QoS.AT_LEAST_ONCE) };
                connection.subscribe(topics, subscriptionCompleteCallback);
                // TODO: Figure this out
                Topic[] topics2 = { new Topic (TopicFactory.monitoring("+"), QoS.AT_LEAST_ONCE) };
                connection.subscribe(topics2, subscriptionCompleteCallback);
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Connection failure", value);
            }
        });

    }

    public void stop() {
        connection.disconnect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                LOGGER.info("Disconnect success");
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Disconnect failure");
            }
        });
    }

    public void publish(String queue, byte[] msg, QoS qos, Callback<Void> callback) {
        if (qos == null) {
            qos = QoS.AT_LEAST_ONCE;
        }
        if (callback == null) {
            callback = new Callback<Void>() {
                @Override
                public void onSuccess(Void value) {

                }

                @Override
                public void onFailure(Throwable value) {
                    LOGGER.info("Error sending message");
                }
            };
        }
        connection.publish(queue, msg, qos, false, callback);
    }
}
