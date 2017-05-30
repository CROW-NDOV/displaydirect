package nl.crowndov.displaydirect.commonclient.mqtt;

import nl.crowndov.displaydirect.common.messages.DisplayDirectMessage;
import nl.crowndov.displaydirect.common.transport.mqtt.TopicFactory;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Copyright 2017 CROW-NDOV
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
public class MqttClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttClient.class);

    private MQTT mqtt;
    private final CallbackConnection connection;

    public MqttClient(String hostname, String uuid, List<String> stopCode, onNewMessage msg) {
        mqtt = new MQTT();
        try {
            mqtt.setHost(hostname);
        } catch (URISyntaxException e) {
           LOGGER.error("Error setting host");
        }
        mqtt.setClientId(uuid);
        mqtt.setCleanSession(true);
        mqtt.setKeepAlive((short) 90);
        byte[] lastWill = DisplayDirectMessage.Unsubscribe.newBuilder().setIsPermanent(false).build().toByteArray();
        mqtt.setWillMessage(new UTF8Buffer(lastWill));
        mqtt.setWillTopic(TopicFactory.unsubscribe(uuid));


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
                DisplayDirectMessage.Subscribe.Builder build = DisplayDirectMessage.Subscribe.newBuilder()
                        .addAllStopCode(stopCode)
                        .setDisplayProperties(DisplayDirectMessage.Subscribe.DisplayProperties.newBuilder().setTextCharacters(0))
                        .setFieldFilter(DisplayDirectMessage.Subscribe.FieldFilter.newBuilder()
                            .setTripStopStatus(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                            .setTargetDepartureTime(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                            .setDestination(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                            .setLinePublicNumber(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS)
                            .setTransportType(DisplayDirectMessage.Subscribe.FieldFilter.Delivery.ALWAYS))
                        .setEmail("joelhaasnoot@gmail.com")
                        .setDescription("Dit is een testdisplay");

                byte[] msg = build.build().toByteArray();

                String topic = TopicFactory.subscribe(uuid);
                LOGGER.info("Sending subscription to {}", topic);
                publish(topic, msg, QoS.AT_MOST_ONCE, null); // IMPORTANT QOS for Subscription
                LOGGER.info("Connection success");

                subscribe(TopicFactory.travelInformation(uuid));
                subscribe(TopicFactory.subscriptionResponse(uuid));
                subscribe(TopicFactory.unsubscribe(uuid));
            }

            @Override
            public void onFailure(Throwable value) {
                LOGGER.info("Connection failure");
            }
        });

    }

    private void subscribe(String subscrTopic) {
        Topic[] topics = { new Topic (subscrTopic, QoS.AT_LEAST_ONCE) };
        connection.subscribe(topics, new Callback<byte[]>() {
            public void onSuccess(byte[] qoses) { LOGGER.info("Subscribe success for topic {}", subscrTopic); }
            public void onFailure(Throwable value) {
                LOGGER.info("Subscribe failure for topic {}", subscrTopic, value);
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

    public interface onNewMessage {
        void onMessage(String topic, byte[] data);
    }
}
