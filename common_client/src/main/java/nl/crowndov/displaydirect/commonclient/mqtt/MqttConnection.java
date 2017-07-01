package nl.crowndov.displaydirect.commonclient.mqtt;

import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright 2017 CROW-NDOV
 * <p>
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MqttConnection {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttConnection.class);

    private final CallbackConnection connection;

    public MqttConnection(CallbackConnection connection) {
        this.connection = connection;
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
                    LOGGER.info("Error sending message:", value);
                }
            };
        }
        connection.publish(queue, msg, qos, false, callback);
    }


    public void subscribe(String subscrTopic, QoS quality) {
        Topic[] topics = { new Topic (subscrTopic, QoS.AT_LEAST_ONCE) };
        connection.subscribe(topics, new Callback<byte[]>() {
            public void onSuccess(byte[] qoses) { LOGGER.info("Subscribe success for topic {}", subscrTopic); }
            public void onFailure(Throwable value) {
                LOGGER.info("Subscribe failure for topic {}", subscrTopic, value);
            }
        });
    }
}
