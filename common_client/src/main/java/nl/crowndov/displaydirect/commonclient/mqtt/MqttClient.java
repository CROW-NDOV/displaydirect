package nl.crowndov.displaydirect.commonclient.mqtt;

import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import nl.crowndov.displaydirect.commonclient.domain.SubscriptionBuilder;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
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

    public MqttClient(String hostname, String uuid, onClientAction msg) {
        mqtt = new MQTT();
        try {
            mqtt.setHost(hostname);
        } catch (URISyntaxException e) {
           LOGGER.error("Error setting host");
        }
        mqtt.setClientId(uuid);
        mqtt.setCleanSession(true);
        mqtt.setKeepAlive((short) 90);
        mqtt.setWillMessage(new UTF8Buffer(SubscriptionBuilder.unsubscribe().toByteArray()));
        mqtt.setWillTopic(TopicFactory.unsubscribe(uuid));


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
                msg.onMessage(topic.toString(), body.toByteArray());
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



    public interface onClientAction {
        void onConnect(MqttConnection connection);
        void onMessage(String topic, byte[] data);
    }
}
